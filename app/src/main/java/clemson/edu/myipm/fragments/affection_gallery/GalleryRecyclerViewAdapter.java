package clemson.edu.myipm.fragments.affection_gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.GalleryDAO;
import clemson.edu.myipm.downloader.ImageLoaderTask;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    private final List<GalleryDAO.GalleryImage> galleryImages;
    private final OnImageSelectionListener mListener;
    private Context mContext;

    public GalleryRecyclerViewAdapter(Context context, List<GalleryDAO.GalleryImage> items, OnImageSelectionListener listener) {
        galleryImages = items;
        mListener = listener;
        mContext = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_affection_gallery_image, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = galleryImages.get(position);
        new ImageLoaderTask(holder.mImageView, mContext, 160, 160).execute(galleryImages.get(position).getImageURL());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (null != mListener) {mListener.onImageSelection(holder.mItem);}
            }
        });
    }

    public int getItemCount() {
        return galleryImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final SquareImageView mImageView;
        public GalleryDAO.GalleryImage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (SquareImageView) view.findViewById(R.id.galleryImage);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
