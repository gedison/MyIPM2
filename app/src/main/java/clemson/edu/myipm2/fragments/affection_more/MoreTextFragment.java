package clemson.edu.myipm2.fragments.affection_more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.database.dao.MoreDAO;
import clemson.edu.myipm2.fragments.core.OnAffectionChangedListener;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

public class MoreTextFragment extends Fragment implements OnAffectionChangedListener{
    private static final String MORE_POSITION= "morePosition";

    private int morePosition;
    private TextView titleView, textView;

    public MoreTextFragment() {}

    public static MoreTextFragment newInstance(int morePosition) {
        MoreTextFragment fragment = new MoreTextFragment();
        Bundle args = new Bundle();
        args.putInt(MORE_POSITION, morePosition);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            morePosition = getArguments().getInt(MORE_POSITION, 0);
        }
    }


    public void onAffectionChanged(){
        String affectionId = SharedPreferencesHelper.getAffectionId(getContext());
        MoreDAO.More more = (new MoreDAO(getContext())).getMore(affectionId);

        String text = "";
        String title = more.getTitleByIndex(morePosition);
        switch(morePosition){
            case 0:
                text = more.getSymptoms();
                break;
            case 1:
                text = more.getChemical();
                break;
            case 2:
                text = more.getFungicide();
                break;
            case 3:
                text = more.getBiological();
                break;
            default:
                text = more.getSymptoms();
                break;
        }

        if(title.isEmpty())titleView.setHeight(0);
        else titleView.setText(Html.fromHtml(title));

        textView.setText(Html.fromHtml(text));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_title_text, container, false);
        titleView = (TextView)root.findViewById(R.id.titleView);
        textView = (TextView)root.findViewById(R.id.textView);
        onAffectionChanged();

        return root;
    }
}
