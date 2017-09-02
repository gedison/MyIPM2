package clemson.edu.myipm.fragments.affection_more;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Gr on 5/19/2015.
 */
public class MoreListAdapter extends ArrayAdapter<String> {

    int res;

    public MoreListAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        res=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(res, null);
        }

        String title = getItem(position);
        if (title != null) {
            ((TextView)v).setText(Html.fromHtml(title));

        }return v;
    }

}
