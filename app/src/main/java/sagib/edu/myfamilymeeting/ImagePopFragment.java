package sagib.edu.myfamilymeeting;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ImagePopFragment extends DialogFragment {


    @BindView(R.id.ivImage)
    SimpleDraweeView ivImage;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_pop, container, false);
        String uri = getArguments().getString("uri");
        ivImage = (SimpleDraweeView) v.findViewById(R.id.ivImage);
        ivImage.setImageURI(uri);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
