package clemson.edu.myipm2.table;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import clemson.edu.myipm2.MainFragmentActivity;
import clemson.edu.myipm2.R;
import clemson.edu.myipm2.database.dao.AffectionDAO;
import clemson.edu.myipm2.database.dao.AffectionSelectionDAO;
import clemson.edu.myipm2.database.dao.FruitDAO;
import clemson.edu.myipm2.database.dao.TableEntry;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

public class TableActivity extends AppCompatActivity implements OnTableTaskComplete {

    private boolean switchVariable = true;
    private Button conButton;
    private Button orButton;

    //db Variables
    private int typeID = 1;
    private int tableType = 0;
    private String pulseRow = "";
    private int activeId = -1;
    private boolean reloadData = false;

    private String[][] rowNames;
    private TableEntry[] data = null;
    private MyTableFragment myTableFragment;
    public List<AffectionSelectionDAO.Affection> affections;

    public static final String TABLE_TYPE = "tableType",
            TYPE_ID = "typeId",
            SPECIAL = "special",
            ACTIVE_ID = "activeId",
            PULSE = "activeName";
    ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_parent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Grab Intent Information
        Intent myIntent = this.getIntent();
        typeID = myIntent.getIntExtra(TYPE_ID, 1);
        tableType = myIntent.getIntExtra(TABLE_TYPE, 0);
        pulseRow = myIntent.getStringExtra(PULSE);
        activeId = myIntent.getIntExtra(ACTIVE_ID, -1);
        if (activeId != -1) System.out.println(activeId);
        else System.out.println("NULL");

        switchVariable = (typeID != 1);
        Boolean spinnerOn = myIntent.getBooleanExtra(SPECIAL, false);

        rowNames = new String[][]{
                //Active
                {
                        getResources().getString(R.string.ai_column1),
                        (SharedPreferencesHelper.getAffectionTypeId(this).equals("1")) ? getResources().getString(R.string.ai_column2) : getResources().getString(R.string.ai_column2_alt),
                        getResources().getString(R.string.ai_column4),
                        getResources().getString(R.string.ai_column3)

                        ,
                },
                //Trade Names
                {
                        getResources().getString(R.string.tn_column1),
                        getResources().getString(R.string.tn_column2),

                        (SharedPreferencesHelper.getAffectionTypeId(this).equals("1")) ? getResources().getString(R.string.tn_column3) : getResources().getString(R.string.tn_column3_alt),
                        getResources().getString(R.string.tn_column26),
                        getResources().getString(R.string.tn_column4),
                        getResources().getString(R.string.tn_column5),
                        getResources().getString(R.string.tn_column6),

                        getResources().getString(R.string.tn_column7),
                        getResources().getString(R.string.tn_column8),
                        getResources().getString(R.string.tn_column9),

                        getResources().getString(R.string.tn_column11),
                        getResources().getString(R.string.tn_column12),
                        getResources().getString(R.string.tn_column13),
                        getResources().getString(R.string.tn_column14),
                        getResources().getString(R.string.tn_column15),
                        getResources().getString(R.string.tn_column16),
                        getResources().getString(R.string.tn_column17),
                        getResources().getString(R.string.tn_column18),
                        getResources().getString(R.string.tn_column19),
                        getResources().getString(R.string.tn_column20),
                        getResources().getString(R.string.tn_column21),
                        getResources().getString(R.string.tn_column22),
                        getResources().getString(R.string.tn_column23),
                        getResources().getString(R.string.tn_column24),
                        getResources().getString(R.string.tn_column25)
                }
        };

        if (SharedPreferencesHelper.getAffectionTypeId(this).equals("2")) {
            rowNames[0] = new String[]{
                    getResources().getString(R.string.ai_column1),
                    (SharedPreferencesHelper.getAffectionTypeId(this).equals("1")) ? getResources().getString(R.string.ai_column2) : getResources().getString(R.string.ai_column2_alt),
                    getResources().getString(R.string.ai_column3)

            };
        }


        //Switch Stuff
        conButton = (Button) findViewById(R.id.check1);
        conButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVariable) {
                    switchVariable = false;
                    changeType(null);

                    conButton.setBackgroundColor(Color.RED);
                    conButton.setTextColor(Color.WHITE);

                    orButton.setBackgroundColor(Color.WHITE);
                    orButton.setTextColor(Color.RED);
                }
            }
        });

        orButton = (Button) findViewById(R.id.check2);
        orButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!switchVariable) {
                    switchVariable = true;
                    changeType(null);

                    conButton.setBackgroundColor(Color.WHITE);
                    conButton.setTextColor(Color.RED);

                    orButton.setBackgroundColor(Color.RED);
                    orButton.setTextColor(Color.WHITE);
                }
            }
        });

        if (switchVariable) {
            conButton.setBackgroundColor(Color.WHITE);
            conButton.setTextColor(Color.RED);
            orButton.setBackgroundColor(Color.RED);
            orButton.setTextColor(Color.WHITE);
        } else {
            conButton.setBackgroundColor(Color.RED);
            conButton.setTextColor(Color.WHITE);
            orButton.setBackgroundColor(Color.WHITE);
            orButton.setTextColor(Color.RED);
        }
        if (spinnerOn) {
            ((LinearLayout) findViewById(R.id.checkHolder)).setVisibility(View.GONE);
            Spinner affectionSelect = (Spinner) findViewById(R.id.affection_select);
            affectionSelect.setVisibility(View.GONE);
        }

        setTableFragment(savedInstanceState);

        TableTask tableTask = new TableTask(this, this, tableType, activeId, SharedPreferencesHelper.getAffectionId(this), typeID);
        tableTask.execute();
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FruitDAO fruitDAO = new FruitDAO(this);
        FruitDAO.Fruit fruit = fruitDAO.getFruitWithId(SharedPreferencesHelper.getFruitId(this));
        int color = Color.parseColor("#" + fruit.getColor());
        toolbar.setBackgroundColor(color);
        changeStatusBarColor(MainFragmentActivity.darker(color, .7f));
        AffectionDAO affectionDAO = new AffectionDAO(this);
        String title = affectionDAO.getAffectionName(SharedPreferencesHelper.getAffectionId(this));
        toolbar.setTitle(title);
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public void onResume() {
        super.onResume();

        Spinner affectionSelect = (Spinner) findViewById(R.id.affection_select);
        affectionSelect.setVisibility(View.VISIBLE);

        AffectionSelectionDAO affectionSelectionDAO = new AffectionSelectionDAO(this);
        affections = affectionSelectionDAO.getAffections(SharedPreferencesHelper.getFruitId(this), SharedPreferencesHelper.getAffectionTypeId(this));
        String[] affectionArray = new String[affections.size() + 1];
        affectionArray[0] = "Select";
        for (int i = 0; i < affections.size(); i++)
            affectionArray[i + 1] = affections.get(i).getName();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.list_item_black_text, affectionArray);
        affectionSelect.setAdapter(adapter);

        affectionSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getChildAt(0) != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    onAffectionChanged(i);
                    adapterView.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        setToolbar();
        if (reloadData) refreshData();
        else reloadData = true;
    }


    public void createBabyTable(int activeID) {
        Intent i = new Intent(getApplication(), TableActivity.class);
        i.putExtra(SPECIAL, true);
        i.putExtra(TYPE_ID, typeID);
        i.putExtra(TABLE_TYPE, 1);
        i.putExtra(ACTIVE_ID, activeID);
        System.out.println(activeID);
        startActivity(i);
    }

    public void createBabyTableActive(int activeID) {
        Intent i = new Intent(getApplication(), TableActivity.class);
        i.putExtra(SPECIAL, true);
        i.putExtra(TYPE_ID, typeID);
        i.putExtra(TABLE_TYPE, 0);
        i.putExtra(ACTIVE_ID, activeID);
        System.out.println(activeID);
        startActivity(i);
    }

    public void setTableFragment(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            myTableFragment = new MyTableFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray("rownames", rowNames[tableType]);
            bundle.putSerializable("data", new TableEntry[0]);
            if (pulseRow != null) bundle.putSerializable("pulseRow", pulseRow);
            myTableFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.table_frame, myTableFragment, "your_fragment_tag").commit();
        } else {
            myTableFragment = (MyTableFragment) getSupportFragmentManager().findFragmentByTag("your_fragment_tag");
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "your_fragment_tag", myTableFragment);
    }

    public void changeType(View v) {
        typeID = (typeID == 1) ? 2 : 1;
        refreshData();
    }

    public void refreshData() {
        TableTask tableTask = new TableTask(this, this, tableType, activeId, SharedPreferencesHelper.getAffectionId(this), typeID);
        tableTask.execute();
    }

    public void onAffectionChanged(int arrayIndex) {
        if (arrayIndex == 0){
            return;
        }

        String affectionId = affections.get(arrayIndex - 1).getAffectionId();
        SharedPreferencesHelper.setAffectionId(this, affectionId);
        refreshData();
    }

    public void showNoDataPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.table_no_data_contents).setTitle(R.string.table_no_data_title)
                .setNegativeButton("Don't show again", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferencesHelper.setTableNoData(getApplicationContext(), false);
                    }
                })
                .setPositiveButton("Okay", null);
        builder.create();
        builder.show();
    }

    public void onTableTaskComplete(TableEntry[] tableEntries) {
        this.data = tableEntries;

        if (data.length == 0 && SharedPreferencesHelper.getTableNoData(this)) {
            showNoDataPopup();
        }

        setToolbar();

        myTableFragment.setTableData(data);
    }
}
