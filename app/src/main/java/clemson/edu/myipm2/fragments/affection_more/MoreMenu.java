package clemson.edu.myipm2.fragments.affection_more;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.database.dao.MoreDAO;
import clemson.edu.myipm2.fragments.affection_menu.ExpandableHeightListView;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;


public class MoreMenu extends Fragment implements AdapterView.OnItemClickListener{
    private OnMoreMenuSelectListener mListener;

    public MoreMenu() {}


    public static MoreMenu newInstance() {
        MoreMenu fragment = new MoreMenu();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_menu, container, false);

        TextView moreTitleView = (TextView) view.findViewById(R.id.more_title);

        String title = (SharedPreferencesHelper.getAffectionTypeId(getContext()).equals("1")) ?
                getText(R.string.more_list_title).toString() : getText(R.string.more_list_title_alt).toString();
        moreTitleView.setText(title);

        String affectionId = SharedPreferencesHelper.getAffectionId(getContext());
        MoreDAO.More more = (new MoreDAO(getContext()).getMore(affectionId));
        String[] moreItemArray = new String[]{((more.getName().equals("") ? "Name" : more.getName())), "Chemical Control", "Specific Resistance Issues", "Non-Chemical Control"};
        ExpandableHeightListView moreList = (ExpandableHeightListView) view.findViewById(R.id.more_list);
        moreList.setOnItemClickListener(this);
        moreList.setAdapter(new MoreListAdapter(getContext(), R.layout.main_list_item, moreItemArray));
        moreList.setExpanded(true);

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoreMenuSelectListener) {
            mListener = (OnMoreMenuSelectListener) context;
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
            case "more":
                mListener.onMoreMenuItemSelected(i);
                break;
        }

    }
}
