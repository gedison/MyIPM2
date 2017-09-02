package clemson.edu.myipm.fragments.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import clemson.edu.myipm.R;

public class TextWithTitleFragment extends Fragment {
    private static final String TEXT_CONTENT = "text_content";
    private static final String TITLE_CONTENT = "title_content";

    private String content;
    private String title;
    public TextWithTitleFragment() {}

    public static TextWithTitleFragment newInstance(String titleContent, String textContent) {
        TextWithTitleFragment fragment = new TextWithTitleFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_CONTENT, titleContent);
        args.putString(TEXT_CONTENT, textContent);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            content = getArguments().getString(TEXT_CONTENT);
            title = getArguments().getString(TITLE_CONTENT);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_title_text, container, false);


        TextView titleView = (TextView)root.findViewById(R.id.titleView);
        if(title.isEmpty())titleView.setHeight(0);
        else titleView.setText(Html.fromHtml(title));

        TextView textView = (TextView)root.findViewById(R.id.textView);
        textView.setText(Html.fromHtml(content));

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }
}
