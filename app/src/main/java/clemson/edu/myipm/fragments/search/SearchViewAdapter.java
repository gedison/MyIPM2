package clemson.edu.myipm.fragments.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.AffectionSelectionDAO;
import clemson.edu.myipm.database.dao.GalleryDAO;
import clemson.edu.myipm.database.dao.SearchDAO;
import clemson.edu.myipm.fragments.affection_gallery.OnImageSelectionListener;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {

    private final List<SearchDAO.SearchResult> searchResults;
    private final SearchFragmentListener mListener;
    private final Context myContext;

    public SearchViewAdapter(List<SearchDAO.SearchResult> items, SearchFragmentListener listener, Context context) {
        searchResults = items;
        mListener = listener;
        myContext = context;
    }

    public void updateData(List<SearchDAO.SearchResult> data){
        searchResults.clear();
        searchResults.addAll(data);
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.result = searchResults.get(position);
        holder.titleView.setText(searchResults.get(position).toString());
        holder.searchContent.removeAllViews();

        Set<String> keys = holder.result.getKeys();

        Drawable backgroundImage = myContext.getResources().getDrawable(R.drawable.info);

        for(String key : keys){
            final List<SearchDAO.AffectionFruit> fruitAffections = holder.result.getAffectionFruitForFruit(key);
            if(fruitAffections.size()<=0)continue;

            LinearLayout titleHolder = new LinearLayout(myContext);
            titleHolder.setOrientation(LinearLayout.HORIZONTAL);
            titleHolder.setPadding(10,0,10,0);

            TextView fruitView = new TextView(myContext);
            fruitView.setText(key);
            fruitView.setTextAppearance(myContext, android.R.style.TextAppearance_Medium);
            fruitView.setTypeface(null, Typeface.BOLD);
            fruitView.setTextColor(Color.parseColor("#"+fruitAffections.get(0).getFruitColor()));
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.weight = 1;
            fruitView.setLayoutParams(p);
            titleHolder.addView(fruitView);

            ImageView link = new ImageView(myContext);
            link.setImageDrawable(backgroundImage);
            LinearLayout.LayoutParams linkParams = new LinearLayout.LayoutParams(80,80);
            link.setLayoutParams(linkParams);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onInfoSelected(fruitAffections.get(0));
                }
            });

            titleHolder.addView(link);
            holder.searchContent.addView(titleHolder);

            for(final SearchDAO.AffectionFruit affection : fruitAffections){
                LinearLayout spacer = new LinearLayout(myContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);

                spacer.setLayoutParams(lp);
                spacer.setBackgroundColor(Color.LTGRAY);
                holder.searchContent.addView(spacer);

                TextView affectionRow = new TextView(myContext);
                affectionRow.setClickable(true);
                TypedValue outValue = new TypedValue();
                myContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                affectionRow.setBackgroundResource(outValue.resourceId);
                affectionRow.setHeight(120);
                affectionRow.setGravity(Gravity.CENTER_VERTICAL);
                affectionRow.setPadding(10,0,0,0);
                affectionRow.setText(affection.toString());
                affectionRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                affectionRow.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        mListener.onItemSelection(affection);
                    }
                });

                holder.searchContent.addView(affectionRow);
            }


        }
    }

    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView;
        public final LinearLayout searchContent;
        public SearchDAO.SearchResult result;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleView = (TextView) view.findViewById(R.id.search_title);
            searchContent = (LinearLayout) view.findViewById(R.id.search_content);
        }
    }
}
