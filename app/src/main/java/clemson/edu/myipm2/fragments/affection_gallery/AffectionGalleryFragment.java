package clemson.edu.myipm2.fragments.affection_gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import clemson.edu.myipm2.R;
import clemson.edu.myipm2.database.dao.GalleryDAO;
import clemson.edu.myipm2.utility.SharedPreferencesHelper;

public class AffectionGalleryFragment extends Fragment {

    private List<GalleryDAO.GalleryImage> galleryImages = new ArrayList<>();
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnImageSelectionListener mListener;
    private RecyclerView recyclerView;

    public AffectionGalleryFragment() {}

    public static AffectionGalleryFragment newInstance(int columnCount) {
        AffectionGalleryFragment fragment = new AffectionGalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_fruitselector_list2, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

            GalleryDAO galleryDAO = new GalleryDAO(getContext());
            String affectionId = SharedPreferencesHelper.getAffectionId(getContext());
            galleryImages = galleryDAO.getImages(affectionId);

            recyclerView.setAdapter(new GalleryRecyclerViewAdapter(context, galleryImages, mListener));
        }

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageSelectionListener) {
            mListener = (OnImageSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
