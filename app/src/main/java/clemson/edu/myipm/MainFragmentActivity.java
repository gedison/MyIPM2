package clemson.edu.myipm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import clemson.edu.myipm.database.DBAdapter;
import clemson.edu.myipm.database.InitDatabaseTask;
import clemson.edu.myipm.database.OnInitFinishedListener;
import clemson.edu.myipm.database.dao.AboutDAO;
import clemson.edu.myipm.database.dao.AffectionDAO;
import clemson.edu.myipm.database.dao.AffectionSelectionDAO;
import clemson.edu.myipm.database.dao.AudioDAO;
import clemson.edu.myipm.database.dao.AutoCompleteTextDAO;
import clemson.edu.myipm.database.dao.GalleryDAO;
import clemson.edu.myipm.database.dao.GuideDAO;
import clemson.edu.myipm.database.dao.MainScreenDAO;
import clemson.edu.myipm.database.dao.ResistanceDAO;
import clemson.edu.myipm.database.dao.SearchDAO;
import clemson.edu.myipm.database.dao.SituationDAO;
import clemson.edu.myipm.downloader.ImageLoaderTask;
import clemson.edu.myipm.fragments.core.OnAffectionChangedListener;
import clemson.edu.myipm.fragments.core.TextFragment;
import clemson.edu.myipm.fragments.affection_addendum.AffectionAddendumPager;
import clemson.edu.myipm.fragments.affection_gallery.MyGestureListener;
import clemson.edu.myipm.fragments.affection_gallery.OnImageSelectionListener;
import clemson.edu.myipm.fragments.affection_more.MoreTextFragment;
import clemson.edu.myipm.fragments.affection_more.OnMoreMenuSelectListener;
import clemson.edu.myipm.fragments.affection_audio.MediaFragment;
import clemson.edu.myipm.fragments.affection_menu.AffectionMenu;
import clemson.edu.myipm.fragments.affection_menu.OnAffectionMenuSelectListener;
import clemson.edu.myipm.fragments.affection_select.AffectionPager;
import clemson.edu.myipm.fragments.affection_select.OnAffectionSelectionListener;
import clemson.edu.myipm.fragments.main_screen.FruitSelectorFragment;
import clemson.edu.myipm.fragments.main_screen.OnFruitSelectionListener;
import clemson.edu.myipm.fragments.resistance_menu.GeneralListFragment;
import clemson.edu.myipm.fragments.resistance_menu.GeneralListSelectionListener;
import clemson.edu.myipm.fragments.search.ArrayAdapterSearchView;
import clemson.edu.myipm.fragments.search.SearchFragment;
import clemson.edu.myipm.fragments.search.SearchFragmentListener;
import clemson.edu.myipm.fragments.welcome.WelcomeFragment;
import clemson.edu.myipm.fragments.toolbar.ToolBarVisibility;
import clemson.edu.myipm.table.TableActivity;
import clemson.edu.myipm.utility.SharedPreferencesHelper;

public class MainFragmentActivity extends AppCompatActivity implements OnInitFinishedListener, SearchFragmentListener, OnImageSelectionListener, OnMoreMenuSelectListener, OnFruitSelectionListener, OnAffectionSelectionListener, OnAffectionMenuSelectListener, GeneralListSelectionListener, ToolBarVisibility {


    public static final String SEARCH_VALUE = "searchValue";
    public static final String NO_SPASH = "noSplash";
    public List<AffectionSelectionDAO.Affection> affections;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button settingsButton = (Button) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DownloaderActivity.class);
                startActivity(i);
            }
        });

        ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) findViewById(R.id.search);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getText(R.string.search_alt));
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_top) instanceof SearchFragment){
                    SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_top);
                    searchFragment.doSearch(query);
                }else switchToSearchFragment(query);

                return false;
            }

            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = ((TextView) view).getText().toString();
                ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) findViewById(R.id.search);
                searchView.setText(query);
                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_top) instanceof SearchFragment) {


                    SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_top);
                    searchFragment.doSearch(query);

                }else switchToSearchFragment(query);

            }
        });

        AutoCompleteTextDAO autoCompleteTextDAO = new AutoCompleteTextDAO(this);
        String[] autoCompleteText = autoCompleteTextDAO.getAutoCompleteText();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_white_text, autoCompleteText);
        searchView.setAutoCompleSuggestionsAdapter(adapter);

        DBAdapter mDBAdapter = new DBAdapter(this);
        boolean noSplash = getIntent().getBooleanExtra(NO_SPASH, false);
        if(!noSplash) {
            final boolean databaseEmpty = mDBAdapter.isDatabaseEmpty();
            if(databaseEmpty){
                setWelcomeFragment();
                new InitDatabaseTask(this, this).execute();
            }else{
                setFruitSelectorFragment();
            }
        }else{
            setFruitSelectorFragment();
        }

        View parent = findViewById(R.id.fragment_parent);
        parent.requestFocus();
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        if (v instanceof AutoCompleteTextView) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            // calculate the relative position of the clicking position against the position of the view
            float x = event.getRawX() - scrcoords[0];
            float y = event.getRawY() - scrcoords[1];

            // check whether action is up and the clicking position is outside of the view
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < 0 || x > v.getRight() - v.getLeft()
                    || y < 0 || y > v.getBottom() - v.getTop())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if(imm.isAcceptingText()) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                View parent = findViewById(R.id.fragment_parent);
                parent.requestFocus();
            }
        }
        return super.dispatchTouchEvent(event);
    }


    public void performFragmentTransaction(boolean backStack, Fragment topFragment, Fragment middleFragment, Fragment bottomFragment){
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();

        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container_hug_bottom) != null){
            if(bottomFragment==null) transaction.remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container_hug_bottom));
            else transaction.replace(R.id.fragment_container_hug_bottom, bottomFragment);
        }else if(bottomFragment!=null){
            transaction.add(R.id.fragment_container_hug_bottom, bottomFragment);
        }


        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container_bottom) != null){
            System.out.println(getSupportFragmentManager().findFragmentById(R.id.fragment_container_bottom).getClass().toString());
            if(middleFragment==null){
                transaction.remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container_bottom));
            }else{
                transaction.replace(R.id.fragment_container_bottom, middleFragment);
            }
        }else if(middleFragment!=null){
            transaction.add(R.id.fragment_container_bottom, middleFragment);
        }

        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container_top) != null){
            if(topFragment==null) transaction.remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container_top));
            else transaction.replace(R.id.fragment_container_top, topFragment);
        }else if(topFragment!=null){
            transaction.add(R.id.fragment_container_top, topFragment);
        }

        if(backStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addSearchBar(){
        ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) findViewById(R.id.search);
        searchView.setVisibility(View.VISIBLE);
    }

    public void removeSearchBar(){
        ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) findViewById(R.id.search);
        searchView.setVisibility(View.GONE);
    }

    public void addDownloadButton(){
        Button button = (Button) findViewById(R.id.settings);
        button.setVisibility(View.VISIBLE);
    }

    public void removeDownloadButton(){
        Button button = (Button) findViewById(R.id.settings);
        button.setVisibility(View.GONE);
    }

    public void addAffectionSelect(){
        Spinner affectionSelect = (Spinner) findViewById(R.id.affection_select);
        affectionSelect.setVisibility(View.VISIBLE);
    }

    public void removeAffectionSelect(){
        Spinner affectionSelect = (Spinner) findViewById(R.id.affection_select);
        affectionSelect.setVisibility(View.GONE);
    }

    public static int darker (int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

    public void changeStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public void changeTitleBar() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_top) != null) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_top);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if (f instanceof FruitSelectorFragment) {
                toolbar.setTitle("MyIPM");
                toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                changeStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
                addSearchBar();
                addDownloadButton();
                removeAffectionSelect();
            }

            if (f instanceof SearchFragment) {
                toolbar.setTitle("Search");
                addSearchBar();
                removeAffectionSelect();
                removeDownloadButton();
            }

            if (f instanceof GeneralListFragment || f instanceof TextFragment || f instanceof WelcomeFragment) {
                removeAffectionSelect();
                removeDownloadButton();
                removeSearchBar();
            }

            if (f instanceof AffectionPager) {
                MainScreenDAO mainScreenDAO = new MainScreenDAO(this);
                MainScreenDAO.MainScreenItem item = mainScreenDAO.getCurrentMainScreenItem(SharedPreferencesHelper.getFruitId(this));

                toolbar.setTitle(item.getFruitName());
                int color = Color.parseColor("#" + item.getColor());
                toolbar.setBackgroundColor(color);
                changeStatusBarColor(darker(color, .7f));
                addAffectionSelect();
                removeDownloadButton();
                removeSearchBar();
            }

            if (f instanceof AffectionAddendumPager || f instanceof MoreTextFragment){
                AffectionDAO affectionDAO = new AffectionDAO(this);
                String title = affectionDAO.getAffectionName(SharedPreferencesHelper.getAffectionId(this));
                toolbar.setTitle(title);
                addAffectionSelect();
                removeDownloadButton();
                removeSearchBar();
            }
        }
    }

    public void setWelcomeFragment(){
        WelcomeFragment welcomeFragment = WelcomeFragment.newInstance();
        performFragmentTransaction(false, welcomeFragment, null, null);
    }

    public void setFruitSelectorFragment(){
        FruitSelectorFragment fruitSelectorFragment = FruitSelectorFragment.newInstance(2);
        fruitSelectorFragment.setArguments(getIntent().getExtras());
        performFragmentTransaction(false, fruitSelectorFragment, null, null);
    }

    public void switchToSearchFragment(String searchTerm){
        SearchFragment mySearchFragment = SearchFragment.newInstance(searchTerm);
        performFragmentTransaction(true, mySearchFragment, null, null);
    }

    public void switchToAffectionSelectorFragment(){
        AffectionPager myPagerFragment = AffectionPager.newInstance();
        AffectionMenu affectionMenuFragment = AffectionMenu.newInstance();
        performFragmentTransaction(true, myPagerFragment, affectionMenuFragment, null);
    }

    public void switchToAffectionFragment(){
        setAffectionToAffectionSelectorValue();

        AudioDAO audioDAO = new AudioDAO(this);
        AudioDAO.Audio audio = audioDAO.getAudio(SharedPreferencesHelper.getAffectionId(this));
        MediaFragment mediaFragment = MediaFragment.newInstance(audio);

        AffectionAddendumPager myPagerFragment = AffectionAddendumPager.newInstance();

        performFragmentTransaction(true, myPagerFragment, null, mediaFragment);
    }

    public void switchToResistanceListFragment(){

        List<String> listItems = new ArrayList<>();
        String fruitId = SharedPreferencesHelper.getFruitId(this);
        String affectionTypeId = SharedPreferencesHelper.getAffectionTypeId(this);

        ResistanceDAO resistanceDAO = new ResistanceDAO(this);
        if(!resistanceDAO.getResistanceString(fruitId, affectionTypeId).isEmpty())
        listItems.add("FAQs");

        GuideDAO guideDAO = new GuideDAO(this);
        if(!guideDAO.getGuideString(fruitId, affectionTypeId).isEmpty())
            listItems.add("Resistance Management Guidelines");

        SituationDAO situationDAO = new SituationDAO(this);
        if(!situationDAO.getSituationString(fruitId, affectionTypeId).isEmpty())
            listItems.add("Situation in the U.S.");

        String[] listContents = new String[listItems.size()];
        for(int i=0; i<listItems.size(); i++)listContents[i] = listItems.get(i);

        GeneralListFragment listFragment = GeneralListFragment.newInstance(listContents);
        performFragmentTransaction(true, listFragment, null, null);
    }

    public void switchToAboutFragment(){
        AboutDAO aboutDAO = new AboutDAO(this);
        String aboutText = aboutDAO.getAboutText();
        TextFragment textFragment = TextFragment.newInstance(aboutText);
        performFragmentTransaction(true, textFragment, null, null);
    }

    public void switchToAffectionActiveFragment(String text){
        TextFragment textFragment = TextFragment.newInstance(text);
        performFragmentTransaction(true, textFragment, null, null);
    }

    public void onListItemSelection(String selectedText) {
        setAffectionIdBeforeChangingFragment();
        String fruitId = SharedPreferencesHelper.getFruitId(this);
        String affectionTypeId = SharedPreferencesHelper.getAffectionTypeId(this);
        String text = "";
        switch(selectedText){
            case "FAQs": {
                ResistanceDAO resistanceDAO = new ResistanceDAO(this);
                text = resistanceDAO.getResistanceString(fruitId, affectionTypeId);
                break;
            }case "Resistance Management Guidelines": {
                GuideDAO guideDAO = new GuideDAO(this);
                text = guideDAO.getGuideString(fruitId, affectionTypeId);
                break;
            }case "Situation in the U.S.": {
                SituationDAO situationDAO = new SituationDAO(this);
                text = situationDAO.getSituationString(fruitId, affectionTypeId);
                break;

            }default: {
                ResistanceDAO resistanceDAO = new ResistanceDAO(this);
                text = resistanceDAO.getResistanceString(fruitId, affectionTypeId);
                break;
            }
        }
        TextFragment textFragment = TextFragment.newInstance(text);

        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container_top, textFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onFruitSelection(MainScreenDAO.MainScreenItem item) {
        SharedPreferencesHelper.setFruitId(this, item.getFruitId());
        SharedPreferencesHelper.setAffectionTypeId(this, item.getAffectionTypeId());

        AffectionSelectionDAO affectionSelectionDAO = new AffectionSelectionDAO(this);
        affections = affectionSelectionDAO.getAffections(SharedPreferencesHelper.getFruitId(this), SharedPreferencesHelper.getAffectionTypeId(this));
        String[] affectionArray = new String[affections.size()+1];
        affectionArray[0] = "Select";
        for(int i=0; i<affections.size(); i++) affectionArray[i+1] = affections.get(i).getName();
        Spinner affectionSelect = (Spinner) findViewById(R.id.affection_select);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.list_item_black_text, affectionArray);
        affectionSelect.setAdapter(adapter);

        affectionSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                onAffectionChanged(i);
                adapterView.setSelection(0);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {}
        });



        switchToAffectionSelectorFragment();
    }

    public void onAffectionChanged(int arrayIndex) {
        if (arrayIndex == 0) return;

        String affectionId = affections.get(arrayIndex - 1).getAffectionId();
        SharedPreferencesHelper.setAffectionId(this, affectionId);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_top) != null) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_top);
            System.out.println(f.getClass().toString()+" ");
            if (f instanceof OnAffectionChangedListener) {
                ((OnAffectionChangedListener) f).onAffectionChanged();
            }
        }

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_hug_bottom) != null) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_hug_bottom);
            if (f instanceof OnAffectionChangedListener) {
                ((OnAffectionChangedListener) f).onAffectionChanged();
            }
        }

        changeTitleBar();
    }

    public void setAffectionToAffectionSelectorValue(){
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_top) != null) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_top);
            if(f instanceof  AffectionPager){
                ((AffectionPager) f).setAffectionId();
            }
        }
    }

    public void switchToTableFragment(int tableType){
       switchToTableFragment(tableType, 1 , "");
    }

    public void switchToTableFragment(int tableType, int typeId, String pulseRow){
        Intent i = new Intent(this, TableActivity.class);
        i.putExtra(TableActivity.TABLE_TYPE, tableType);
        i.putExtra(TableActivity.TYPE_ID, typeId);
        if(!pulseRow.isEmpty()) i.putExtra(TableActivity.PULSE, pulseRow);
        startActivity(i);
    }

    public void createFeedbackIntent(){
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"schnabe@clemson.edu"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MyIPM");
        startActivity(Intent.createChooser(emailIntent, "Contact MyIPM"));
    }

    public void onAffectionMenuItemSelected(int position) {
        setAffectionIdBeforeChangingFragment();
        switch(position){
            case 0:
                switchToAffectionFragment();
                break;
            case 1:
                setAffectionToAffectionSelectorValue();
                switchToTableFragment(0);
                break;
            case 2:
                setAffectionToAffectionSelectorValue();
                switchToTableFragment(1);
                break;
            case 3:
                switchToResistanceListFragment();
                break;
            case 4:
                switchToAboutFragment();
                break;
            case 5:
                createFeedbackIntent();
                break;
        }
    }





    public void setAffectionIdBeforeChangingFragment(){

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_top) != null) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container_top);
            if(f instanceof AffectionPager){
                ((AffectionPager)f).setAffectionId();
            }
        }
    }

    public void onAffectionSelection(String affectionId) {
        SharedPreferencesHelper.setAffectionId(this, affectionId);
        switchToAffectionFragment();
    }

    public void onMoreMenuItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MoreTextFragment textFragment = MoreTextFragment.newInstance(position);
        transaction.replace(R.id.fragment_container_top, textFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    PopupWindow pop;
    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;
    public void onImageSelection(GalleryDAO.GalleryImage item) {
        System.out.println(item.getImageURL());

        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View parent = inflater.inflate(R.layout.gallery_popup, null, false);

        //Variables/Set Image
        LinearLayout imageContainer = (LinearLayout)parent.findViewById(R.id.imageContainer);

        //Get Screen Size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ImageView myImage = (ImageView)parent.findViewById(R.id.myImage);
        System.out.println(size.x+" "+size.y);
        new ImageLoaderTask(myImage, this, 300, 300).execute(item.getImageURL());
        myImage.setLayoutParams(new LinearLayout.LayoutParams(size.x, ViewGroup.LayoutParams.WRAP_CONTENT));


        //Listeners
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        myImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scaleGestureDetector.onTouchEvent(motionEvent);
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        MyGestureListener myGestureListener = new MyGestureListener(myImage, size);
        gestureDetector = new GestureDetector(this, myGestureListener);
        scaleGestureDetector = new ScaleGestureDetector(this, myGestureListener);
        imageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pop!=null)pop.dismiss();
            }
        });

        pop = new PopupWindow(parent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setOutsideTouchable(true);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAtLocation((findViewById(R.id.fragment_parent)), Gravity.CENTER, 0, 0);
    }



    public void onItemSelection(SearchDAO.AffectionFruit result){
        SharedPreferencesHelper.setAffectionId(this, result.getAffectionId());
        SharedPreferencesHelper.setFruitId(this, result.getFruitId());
        SharedPreferencesHelper.setAffectionTypeId(this, result.getAffectionTypeId());
        switchToTableFragment(0, Integer.parseInt(result.getTypeId()), result.getActiveId());
    }


    public void onInfoSelected(SearchDAO.AffectionFruit result) {
        String fruitId = result.getFruitId();
        String activeId = result.getActiveId();
        String text = "N/A";
        switchToAffectionActiveFragment(text);
    }



    public void onInitFinished() {
        setFruitSelectorFragment();
        Intent i = new Intent(getApplicationContext(), DownloaderActivity.class);
        startActivity(i);
    }
}
