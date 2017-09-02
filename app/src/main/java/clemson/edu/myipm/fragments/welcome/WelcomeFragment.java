package clemson.edu.myipm.fragments.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import clemson.edu.myipm.R;
import clemson.edu.myipm.fragments.toolbar.ToolBarVisibility;

public class WelcomeFragment extends Fragment {

    private ToolBarVisibility toolBarVisibilityListener;

    public WelcomeFragment() {}

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_welcome, container, false);
        return root;
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
        toolBarVisibilityListener.changeTitleBar();
    }
}
