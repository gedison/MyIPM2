package clemson.edu.myipm2.fragments.affection_select;

/**
 * Created by gedison on 6/18/2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import clemson.edu.myipm2.database.dao.AffectionSelectionDAO;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

public class AffectionPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<AffectionSelectionDAO.Affection> affectionList;

    public AffectionPagerAdapter(Context context, FragmentManager mgr) {
        super(mgr);
        mContext = context;
        AffectionSelectionDAO affectionSelectionDAO = new AffectionSelectionDAO(context);
        String fruitId = SharedPreferencesHelper.getFruitId(mContext);
        String affectionTypeId = SharedPreferencesHelper.getAffectionTypeId(mContext);
        affectionList = affectionSelectionDAO.getAffections(fruitId, affectionTypeId);
    }


    public int getCount() {
        return affectionList.size()+2;
    }


    public Fragment getItem(int position) {
        if(position == 0)position = affectionList.size()-1;
        else if(position == affectionList.size()+1) position = 0;
        else position--;

        return (AffectionSelectionFragment.newInstance(position, affectionList.get(position)));
    }






}
