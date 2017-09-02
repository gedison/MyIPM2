package clemson.edu.myipm.fragments.main_screen;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.MainScreenDAO;
import java.util.List;

public class MyFruitSelectorRecyclerViewAdapter extends RecyclerView.Adapter<MyFruitSelectorRecyclerViewAdapter.ViewHolder> {

    private final List<MainScreenDAO.MainScreenItem> mainScreenItems;
    private final OnFruitSelectionListener mListener;

    public MyFruitSelectorRecyclerViewAdapter(List<MainScreenDAO.MainScreenItem> items, OnFruitSelectionListener listener) {
        mainScreenItems = items;
        mListener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fruitselector_noimage, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        System.out.println(position+" "+mainScreenItems.get(position).toString());
        holder.mItem = mainScreenItems.get(position);
        holder.mContentView.setText(mainScreenItems.get(position).getName());
        holder.mContentView.setTextColor(Color.parseColor("#"+mainScreenItems.get(position).getColor()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (null != mListener) {mListener.onFruitSelection(holder.mItem);}
            }
        });
    }

    public int getItemCount() {
        return mainScreenItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public MainScreenDAO.MainScreenItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
