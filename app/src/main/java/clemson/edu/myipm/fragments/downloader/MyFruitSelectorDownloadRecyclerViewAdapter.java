package clemson.edu.myipm.fragments.downloader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.AppDAO;
import clemson.edu.myipm.database.dao.MainScreenDAO;

public class MyFruitSelectorDownloadRecyclerViewAdapter extends RecyclerView.Adapter<MyFruitSelectorDownloadRecyclerViewAdapter.ViewHolder> {

    private final List<AppDAO.AppFruit> appItems;
    private final OnFruitSelectionDownloadListener mListener;
    private Context context;

    public MyFruitSelectorDownloadRecyclerViewAdapter(List<AppDAO.AppFruit> items, OnFruitSelectionDownloadListener listener, Context context) {
        appItems = items;
        mListener = listener;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_download_selector_2, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        System.out.println(position+" "+ appItems.get(position).toString());
        holder.mItem = appItems.get(position);
        holder.mContentView.setText(appItems.get(position).toString());
        holder.mContentView.setTextColor(Color.parseColor("#"+ appItems.get(position).getColor()));

        List<AppDAO.App> apps = appItems.get(position).getApps();
        for(AppDAO.App app : apps){
            switch (app.getAffectionTypeId()){
                case "1":
                    holder.checkBox1.setClickable(true);
                    holder.checkBox1.setEnabled(true);
                    holder.disease = app;
                    if(app.getIsDownloaded()){
                        holder.checkBox1.setChecked(true);
                    }else{
                       holder.checkBox1.setChecked(false);
                    }

                    holder.checkBox1.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view) {
                            if (null != mListener) {mListener.onFruitSelection(holder.disease);}
                        }
                    });


                    break;
                case "2":
                    holder.checkBox2.setClickable(true);
                    holder.checkBox2.setEnabled(true);
                    holder.pest = app;
                    if(app.getIsDownloaded()){
                        holder.checkBox2.setChecked(true);
                    }else{
                        holder.checkBox2.setChecked(false);
                    }

                    holder.checkBox2.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view) {
                            if (null != mListener) {mListener.onFruitSelection(holder.pest);}
                        }
                    });
            }
        }
    }

    public int getItemCount() {
        return appItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final CheckBox checkBox1;
        public final CheckBox checkBox2;


        public AppDAO.AppFruit mItem;
        public AppDAO.App pest;
        public AppDAO.App disease;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContentView = (TextView) view.findViewById(R.id.fruitName);
            checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
            checkBox1.setClickable(false);
            checkBox1.setEnabled(false);
            checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
            checkBox2.setClickable(false);
            checkBox2.setEnabled(false);
        }
    }
}
