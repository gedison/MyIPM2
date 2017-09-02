package clemson.edu.myipm.fragments.affection_addendum;

/**
 * Created by gedison on 6/18/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.AudioDAO;
import clemson.edu.myipm.fragments.core.OnAffectionChangedListener;
import clemson.edu.myipm.fragments.toolbar.ToolBarVisibility;
import clemson.edu.myipm.utility.SharedPreferencesHelper;

public class AffectionAddendumPager extends Fragment implements OnAffectionChangedListener{

    ViewPager pager;
    PagerAdapter adapter;
    ToolBarVisibility toolBarVisibilityListener;

    public static AffectionAddendumPager newInstance() {
        AffectionAddendumPager fragment = new AffectionAddendumPager();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.affection_pager, container, false);
        pager=(ViewPager)result.findViewById(R.id.pager);
        adapter = buildAdapter();
        pager.setAdapter(adapter);

        return(result);
    }

    private PagerAdapter buildAdapter() {
        return(new AffectionAddendumPagerAdapter(getActivity(), getChildFragmentManager()));
    }

    public void onAffectionChanged() {
        AudioDAO audioDAO = new AudioDAO(getContext());
        boolean hasAudio = audioDAO.doesAffectionHaveAudio(SharedPreferencesHelper.getAffectionId(getContext()));
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT,  FrameLayout.LayoutParams.WRAP_CONTENT);
        if(hasAudio){
            params.setMargins(0, 0, 0, 175);
            pager.setLayoutParams(params);
        }else{
            params.setMargins(0, 0, 0, 0);
            pager.setLayoutParams(params);
        }

        adapter.notifyDataSetChanged();
    }


    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolBarVisibility) {
            toolBarVisibilityListener = (ToolBarVisibility) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onResume(){
        super.onResume();
        onAffectionChanged();
        toolBarVisibilityListener.changeTitleBar();
    }
}
