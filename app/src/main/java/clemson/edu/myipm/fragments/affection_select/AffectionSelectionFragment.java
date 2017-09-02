package clemson.edu.myipm.fragments.affection_select;

/**
 * Created by gedison on 6/18/2017.
 */


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import clemson.edu.myipm.R;
import clemson.edu.myipm.database.dao.AffectionSelectionDAO;
import clemson.edu.myipm.downloader.ImageLoaderTask;
import clemson.edu.myipm.fragments.main_screen.OnFruitSelectionListener;

public class AffectionSelectionFragment extends Fragment {
    private static final String KEY_POSITION="position";
    private static final String KEY_NAME="affection_name";
    private static final String KEY_ID="affection_id";
    private static final String KEY_URL="image_url";
    private OnAffectionSelectionListener mListener;

    private String id, name, imageUrl;

    static AffectionSelectionFragment newInstance(int position, AffectionSelectionDAO.Affection affection) {
        AffectionSelectionFragment frag=new AffectionSelectionFragment();
        Bundle args=new Bundle();

        args.putInt(KEY_POSITION, position);
        args.putString(KEY_ID, affection.getAffectionId());
        args.putString(KEY_NAME, affection.getName());
        args.putString(KEY_URL, affection.getImageURL());
        frag.setArguments(args);

        return(frag);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(KEY_NAME);
            id = getArguments().getString(KEY_ID);
            imageUrl = getArguments().getString(KEY_URL);
        }
    }



    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFruitSelectionListener) {
            mListener = (OnAffectionSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_affection_selection, container, false);
        result.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mListener.onAffectionSelection(id);
            }
        });

        TextView affectionTitleView = (TextView)result.findViewById(R.id.affection_selection_title);
        affectionTitleView.setText(name);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = (width * 8 )/12;
        ImageView affectionImageView = (ImageView)result.findViewById(R.id.affection_selection_image);
        affectionImageView.setMaxWidth(width);
        affectionImageView.setMinimumWidth(width);
        affectionImageView.setMaxHeight(height);
        affectionImageView.setMinimumHeight(height);

        System.out.println("THIS IS THE IMAGE THAT SHOULD BE LOADED: "+imageUrl);
        new ImageLoaderTask(affectionImageView,
                getContext(),
                ((width>300) ? 300 : width),
                (height>200) ? 200 : height).execute(imageUrl);


        return(result);
    }
}
