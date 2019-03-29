package clemson.edu.myipm2.fragments.affection_menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.fragments.toolbar.ToolBarVisibility;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;


public class AffectionMenu extends Fragment implements AdapterView.OnItemClickListener{
    private OnAffectionMenuSelectListener mListener;
    private ToolBarVisibility toolBarVisibilityListener;

    public AffectionMenu() {}


    public static AffectionMenu newInstance() {
        AffectionMenu fragment = new AffectionMenu();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affection_menu, container, false);

        String[] diagnosticItemArray = new String[]{"Overview/Gallery/More","Active Ingredients","Trade Names"};
        ExpandableHeightListView diagnosticList = (ExpandableHeightListView) view.findViewById(R.id.diagnostics_list);
        diagnosticList.setOnItemClickListener(this);
        diagnosticList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.centered_list_item, diagnosticItemArray));
        diagnosticList.setExpanded(true);

        String[] generalItemArray = new String[]{
                (SharedPreferencesHelper.getAffectionTypeId(getContext()).equals("1")) ? "Pesticide Resistance" : "Insecticide Resistance",
                "About MyIPM",
                "Feedback"};
        ExpandableHeightListView generalList = (ExpandableHeightListView) view.findViewById(R.id.general_list);
        generalList.setOnItemClickListener(this);
        generalList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.centered_list_item, generalItemArray));
        generalList.setExpanded(true);


        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAffectionMenuSelectListener) {
            mListener = (OnAffectionMenuSelectListener) context;
            toolBarVisibilityListener = (ToolBarVisibility) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String tag = (String) adapterView.getTag();
        switch (tag){
            case "diagnostic":
                mListener.onAffectionMenuItemSelected(i);
                System.out.println("Diagnostic: "+i);
                break;
            case "general":
                mListener.onAffectionMenuItemSelected(i+3);
                System.out.println("General: "+i);
                break;
        }

    }

    public void onResume(){
        super.onResume();
        toolBarVisibilityListener.changeTitleBar();
    }

    public void onPause(){
        super.onPause();
        toolBarVisibilityListener.changeTitleBar();
    }



}
