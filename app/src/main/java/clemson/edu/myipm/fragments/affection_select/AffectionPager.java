package clemson.edu.myipm.fragments.affection_select;

/**
 * Created by gedison on 6/18/2017.
 */

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.AffectionSelectionDAO;
import clemson.edu.myipm.fragments.core.OnAffectionChangedListener;
import clemson.edu.myipm.utility.SharedPreferencesHelper;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class AffectionPager extends Fragment implements ViewPager.OnPageChangeListener, OnAffectionChangedListener{

    private List<AffectionSelectionDAO.Affection> affectionList;
    private ViewPager mPager;
    private PagerAdapter adapter;
    private LinearLayout dotHolder;
    private LinearLayout.LayoutParams params_dot;
    private LinearLayout.LayoutParams params_dot_s;

    public static AffectionPager newInstance() {
        AffectionPager fragment = new AffectionPager();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("ON CREATE");
        View result = inflater.inflate(R.layout.affection_selection_pager, container, false);

        AffectionSelectionDAO affectionSelectionDAO = new AffectionSelectionDAO(getContext());
        String fruitId = SharedPreferencesHelper.getFruitId(getContext());
        String affectionTypeId = SharedPreferencesHelper.getAffectionTypeId(getContext());
        affectionList = affectionSelectionDAO.getAffections(fruitId, affectionTypeId);

        mPager = (ViewPager)result.findViewById(R.id.pager);
        mPager.setAdapter(buildAdapter());
        mPager.addOnPageChangeListener(this);

        dotHolder = (LinearLayout) result.findViewById(R.id.dotHolder);
        params_dot = new LinearLayout.LayoutParams(this.dpToPx(8), this.dpToPx(8));
        params_dot.setMargins(8,2,8,2);
        params_dot_s = new LinearLayout.LayoutParams(this.dpToPx(14), this.dpToPx(14));
        params_dot_s.setMargins(20,4,20,4);

        for(int i=0; i<affectionList.size(); i++) {
            ImageView circle = new ImageView(getContext());
            if(i==(mPager.getCurrentItem()-1)){
                circle.setImageResource(R.drawable.circle_selected);
                circle.setLayoutParams(params_dot_s);
            }else{
                circle.setImageResource(R.drawable.circle);
                circle.setLayoutParams(params_dot);
            }dotHolder.addView(circle);
        }

        onAffectionChanged();

        return(result);
    }

    private PagerAdapter buildAdapter() {
        adapter = new AffectionPagerAdapter(getActivity(), getChildFragmentManager());
        return adapter;
    }

    public void onPageSelected(int position) {

        System.out.println("ON PAGE SELECTED: "+position);
        if(position == 0) mPager.setCurrentItem((affectionList.size()),false);
        else if(position == affectionList.size()+1)mPager.setCurrentItem(1, false);

        for(int i=0; i<affectionList.size(); i++) {
            ImageView circle = (ImageView)dotHolder.getChildAt(i);
            if(i==(mPager.getCurrentItem()-1)){
                circle.setImageResource(R.drawable.circle_selected);
                circle.setLayoutParams(params_dot_s);
            }else{
                circle.setImageResource(R.drawable.circle);
                circle.setLayoutParams(params_dot);
            }
        }
    }

    public void onPageScrollStateChanged(int state) {}
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onResume(){
        super.onResume();

        System.out.println("ON resume");
        onAffectionChanged();
    }

    public void setAffectionId(){
        int position = mPager.getCurrentItem();
        position--;
        SharedPreferencesHelper.setAffectionId(getContext(), affectionList.get(position).getAffectionId());
    }


    public void onAffectionChanged() {

        boolean found = false;
        int size = adapter.getCount();
        String currentId = SharedPreferencesHelper.getAffectionId(getContext());
        System.out.println(currentId);
        for(int i=1; i<size-1; i++){
            String selectedId = affectionList.get(i-1).getAffectionId();
            System.out.println(i+" "+selectedId);
            if(currentId.equals(selectedId)){
                found = true;
                mPager.setCurrentItem(i);
                break;
            }
        }

        if(!found){
            mPager.setCurrentItem(0);
            SharedPreferencesHelper.setAffectionId(getContext(), affectionList.get(mPager.getCurrentItem()).getAffectionId());
        }
    }
}
