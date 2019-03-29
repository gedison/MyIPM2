package clemson.edu.myipm2.fragments.downloader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.database.GetAppsFromWebTask;
import clemson.edu.myipm2.database.OnGetAppsFinishedListener;
import clemson.edu.myipm2.database.dao.AppDAO;

public class FruitSelectorDownloadFragment extends Fragment implements OnGetAppsFinishedListener {

    private List<AppDAO.AppFruit> appItems = new ArrayList<AppDAO.AppFruit>();
    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 2;
    private OnFruitSelectionDownloadListener mListener;
    private RecyclerView recyclerView;

    public FruitSelectorDownloadFragment() {}

    public static FruitSelectorDownloadFragment newInstance(int columnCount) {
        FruitSelectorDownloadFragment fragment = new FruitSelectorDownloadFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("On Create "+appItems.size());

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        new GetAppsFromWebTask(this).execute();





        AppDAO appDAO = new AppDAO(getContext());
        appItems  = appDAO.getAppItems();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("Creating view "+appItems.size());
        View view = inflater.inflate(R.layout.fragment_fruitselector_list2, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
            recyclerView.setAdapter(new MyFruitSelectorDownloadRecyclerViewAdapter(appItems, mListener, context));
        }

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFruitSelectionDownloadListener) {
            mListener = (OnFruitSelectionDownloadListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onGetApps(List<AppDAO.App> apps) {
        AppDAO appDAO = new AppDAO(getContext());
        if (!apps.isEmpty()) {
            this.appItems = appDAO.getAppItems(apps);
            recyclerView.setAdapter(new MyFruitSelectorDownloadRecyclerViewAdapter(appItems, mListener, getContext()));

        }
    }


}
