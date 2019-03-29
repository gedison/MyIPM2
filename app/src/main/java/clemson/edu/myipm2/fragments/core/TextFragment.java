package clemson.edu.myipm2.fragments.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.fragments.toolbar.ToolBarVisibility;

public class TextFragment extends Fragment {
    private static final String TEXT_CONTENT = "text_content";
    private String content;
    private TextView textView;
    private ToolBarVisibility toolBarVisibilityListener;

    public TextFragment() {}

    public static TextFragment newInstance(String textContent) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(TEXT_CONTENT, textContent);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            content = getArguments().getString(TEXT_CONTENT);
        }
    }

    public void setText(String text){
        content = text;
        if(textView!=null) {
            URLImageParser urlImageParser = new URLImageParser(textView, getContext());
            Spanned htmlSpan = Html.fromHtml(content, urlImageParser, null);
            textView.setText(htmlSpan);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_text, container, false);
        textView = (TextView)root.findViewById(R.id.textView);
        setText(content);
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
