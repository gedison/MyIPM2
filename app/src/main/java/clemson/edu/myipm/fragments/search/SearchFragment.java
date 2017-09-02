package clemson.edu.myipm.fragments.search;

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

import clemson.edu.myipm.MainFragmentActivity;
import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.SearchDAO;
import clemson.edu.myipm.fragments.toolbar.ToolBarVisibility;

public class SearchFragment extends Fragment {

    private List<SearchDAO.SearchResult> searchResults = new ArrayList<>();
    private SearchFragmentListener mListener;
    private ToolBarVisibility toolBarListener;
    private RecyclerView recyclerView;
    private String searchValue = "";

    public SearchFragment() {}

    public static SearchFragment newInstance(String searchValue) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(MainFragmentActivity.SEARCH_VALUE, searchValue);
        fragment.setArguments(args);
        return fragment;
    }

    public void doSearch(String query){

        SearchDAO searchDAO = new SearchDAO(getContext());
        searchResults = searchDAO.getResultsFromSearch(query);
        SearchViewAdapter myAdapter = (SearchViewAdapter) recyclerView.getAdapter();
        myAdapter.updateData(searchResults);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchValue = getArguments().getString(MainFragmentActivity.SEARCH_VALUE);
        }

        SearchDAO searchDAO = new SearchDAO(getContext());
        searchResults = searchDAO.getResultsFromSearch(searchValue);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
            recyclerView.setAdapter(new SearchViewAdapter(searchResults, mListener, getContext()));
        }

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentListener) {
            mListener = (SearchFragmentListener) context;
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
    }
}
