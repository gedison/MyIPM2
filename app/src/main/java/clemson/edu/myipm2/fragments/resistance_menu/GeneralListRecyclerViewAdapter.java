package clemson.edu.myipm2.fragments.resistance_menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import clemson.edu.myipm2.R;

public class GeneralListRecyclerViewAdapter extends RecyclerView.Adapter<GeneralListRecyclerViewAdapter.ViewHolder> {

    private final String[] listItems;
    private final GeneralListSelectionListener mListener;

    public GeneralListRecyclerViewAdapter(String[] items, GeneralListSelectionListener listener) {
        listItems = items;
        mListener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_generalselector, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mContentView.setText(listItems[position]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (null != mListener) {mListener.onListItemSelection(listItems[position]);}
            }
        });
    }

    public int getItemCount() {
        return listItems.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;


        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
