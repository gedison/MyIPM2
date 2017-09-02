package clemson.edu.myipm.fragments.resistance_menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.MainScreenDAO;
import clemson.edu.myipm.fragments.main_screen.MyFruitSelectorRecyclerViewAdapter;
import clemson.edu.myipm.fragments.main_screen.OnFruitSelectionListener;
import clemson.edu.myipm.fragments.toolbar.ToolBarVisibility;

public class GeneralListFragment extends Fragment {
    private String[] listItems;
    private static final String ARG_LIST_ITEMS = "list-items";

    private GeneralListSelectionListener mListener;
    private ToolBarVisibility  toolBarVisibilityListener;
    private RecyclerView recyclerView;

    public GeneralListFragment() {}

    public static GeneralListFragment newInstance(String[] listItems) {
        GeneralListFragment fragment = new GeneralListFragment();
        Bundle args = new Bundle();

        args.putStringArray(ARG_LIST_ITEMS, listItems);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listItems = getArguments().getStringArray(ARG_LIST_ITEMS);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_generalselector_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            recyclerView.setAdapter(new GeneralListRecyclerViewAdapter(listItems, mListener));
        }

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GeneralListSelectionListener) {
            mListener = (GeneralListSelectionListener) context;
            toolBarVisibilityListener = (ToolBarVisibility) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    public void onResume(){
        super.onResume();
        toolBarVisibilityListener.changeTitleBar();
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
