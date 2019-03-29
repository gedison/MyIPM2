package clemson.edu.myipm2.fragments.main_screen;

import android.content.Context;
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
import clemson.edu.myipm2.database.dao.MainScreenDAO;
import clemson.edu.myipm2.fragments.toolbar.ToolBarVisibility;

public class FruitSelectorFragment extends Fragment {

    private List<MainScreenDAO.MainScreenItem> mainScreenItems = new ArrayList<>();
    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 2;
    private OnFruitSelectionListener mListener;
    private ToolBarVisibility toolBarListener;
    private RecyclerView recyclerView;

    public FruitSelectorFragment() {}

    public void addItemsToGrid(List<MainScreenDAO.MainScreenItem> items){
        for(MainScreenDAO.MainScreenItem item : items)mainScreenItems.add(item);
    }

    public static FruitSelectorFragment newInstance(int columnCount) {
        FruitSelectorFragment fragment = new FruitSelectorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("On Create "+mainScreenItems.size());

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        MainScreenDAO mainScreenDAO = new MainScreenDAO(getContext());
        mainScreenItems  = mainScreenDAO.getMainScreenItems();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("Creating view "+mainScreenItems.size());
        View view = inflater.inflate(R.layout.fragment_fruitselector_list, container, false);

        View listView = view.findViewById(R.id.list);
        if (listView instanceof RecyclerView) {
            Context context = listView.getContext();
            recyclerView = (RecyclerView) listView;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        }

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFruitSelectionListener) {
            mListener = (OnFruitSelectionListener) context;
            toolBarListener = (ToolBarVisibility) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onPause(){
        super.onPause();
        toolBarListener.changeTitleBar();
    }

    public void onResume(){
        super.onResume();
        toolBarListener.changeTitleBar();

        MainScreenDAO mainScreenDAO = new MainScreenDAO(getContext());
        mainScreenItems  = mainScreenDAO.getMainScreenItems();
        recyclerView.setAdapter(new MyFruitSelectorRecyclerViewAdapter(mainScreenItems, mListener));

    }
}
