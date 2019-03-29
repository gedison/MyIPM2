package clemson.edu.myipm2.fragments.affection_addendum;

/**
 * Created by gedison on 6/18/2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import clemson.edu.myipm2.database.dao.OverviewDAO;
import clemson.edu.myipm2.fragments.core.OnAffectionChangedListener;
import clemson.edu.myipm2.fragments.core.TextFragment;
import clemson.edu.myipm2.fragments.affection_gallery.AffectionGalleryFragment;
import clemson.edu.myipm2.fragments.affection_more.MoreMenu;
import clemson.edu.myipm2.fragments.resistance_menu.GeneralListFragment;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

public class AffectionAddendumPagerAdapter extends FragmentStatePagerAdapter implements OnAffectionChangedListener{
    private Context mContext;

    public AffectionAddendumPagerAdapter(Context context, FragmentManager mgr) {
        super(mgr);
        mContext = context;
    }

    public int getCount() {
        return 3;
    }

    public String getPageTitle(int position) {
        switch (position) {
            case 0:return "OVERVIEW";
            case 1:return "GALLERY";
            case 2:return "MORE";
            default: return "OVERVIEW";
        }
    }

    public Fragment getItem(int position) {
        String[] listContents = new String[]{"Name","Chemical Control", "Specific Resistance Issues", "Non-Chemical Control"};

        String affectionId = SharedPreferencesHelper.getAffectionId(mContext);
        switch (position){
            case 0: {
                OverviewDAO overviewDAO = new OverviewDAO(mContext);
                String overview = overviewDAO.getOverviewText(affectionId);
                TextFragment overviewFragment = TextFragment.newInstance(overview);
                return overviewFragment;
            }case 1: {
                AffectionGalleryFragment galleryFragment = AffectionGalleryFragment.newInstance(2);
                return galleryFragment;
            }case 2: {
                MoreMenu moreMenuFragment = MoreMenu.newInstance();
                return moreMenuFragment;
            }default: {
                GeneralListFragment listFragment = GeneralListFragment.newInstance(listContents);
                return listFragment;
            }
        }

    }


    @Override
    public void onAffectionChanged() {


        System.out.println("On Affection Changed");
        String affectionId = SharedPreferencesHelper.getAffectionId(mContext);
        System.out.println(affectionId);
        OverviewDAO overviewDAO = new OverviewDAO(mContext);
        String overview = overviewDAO.getOverviewText(affectionId);
        Fragment f = getItem(0);
        ((TextFragment) f).setText(overview);
        System.out.println("on affection chagned");
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
