package sagib.edu.myfamilymeeting;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class OpenFragment extends Fragment {


    @BindView(R.id.tvTextDate)
    TextView tvTextDate;
    @BindView(R.id.tvOpening)
    TextView tvOpening;
    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.tvEndingText)
    TextView tvEndingText;
    SharedPreferences prefs;
    Unbinder unbinder;
    @BindView(R.id.rvBodyImages)
    RecyclerView rvBodyImages;
    int bodyImagesCount;
    int openImagesCount;
    int foodImagesCount;
    Gson gson;
    @BindView(R.id.rvOpenImages)
    RecyclerView rvOpenImages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_open, container, false);
        unbinder = ButterKnife.bind(this, v);
        gson = new Gson();
        ImagePopup imagePopup = new ImagePopup(getContext());
        prefs = getContext().getSharedPreferences("Values", Context.MODE_PRIVATE);

        bodyImagesCount = Integer.valueOf(prefs.getString("bodyImages", "0"));
        openImagesCount = Integer.valueOf(prefs.getString("openImages", "0"));
        foodImagesCount = Integer.valueOf(prefs.getString("foodImages", "0"));

        ArrayList<String> images = gson.fromJson(prefs.getString("allImages", ""), new TypeToken<ArrayList<String>>() {
        }.getType());
        SubstringComparator comparator = new SubstringComparator();
        if (images != null)
            Collections.sort(images, comparator);
        ArrayList<String> openImages = new ArrayList<>();
        for (int i = 0; i < openImagesCount; i++) {
            openImages.add(images.get(i));
        }
        ArrayList<String> bodyImages = new ArrayList<>();
        for (int i = openImagesCount; i >= openImagesCount && i < bodyImagesCount + openImagesCount; i++) {
            bodyImages.add(images.get(i));
        }

        if (images != null) {
            Log.d("SagiB images", images.toString());
            Log.d("SagiB open", openImages.toString());
            Log.d("SagiB body", bodyImages.toString());
        }

        BodyImagesAdapter bodyImagesAdapter = new BodyImagesAdapter(getContext(), bodyImages, this);
        rvBodyImages.setAdapter(bodyImagesAdapter);
        rvBodyImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        BodyImagesAdapter openImagesAdapter = new BodyImagesAdapter(getContext(), openImages, this);
        rvOpenImages.setAdapter(openImagesAdapter);
        rvOpenImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tvTextDate.setText(prefs.getString("textDate", ""));
//        tvHeader.setText(prefs.getString("header", ""));
        tvOpening.setText(prefs.getString("opening", ""));
        tvBody.setText(prefs.getString("body", ""));
        tvEndingText.setText(prefs.getString("endingText", ""));
        rvOpenImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        rvBodyImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class SubstringComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            String s1 = o1.toString().substring(o1.toString().indexOf("image"), o1.toString().indexOf(".jpg"));
            Log.d("SagiB s1", s1);
            String s2 = o2.toString().substring(o2.toString().indexOf("image"), o2.toString().indexOf(".jpg"));
            Log.d("SagiB s2", s2);
            return s1.compareTo(s2);
        }
    }

    static class BodyImagesAdapter extends RecyclerView.Adapter<BodyImagesAdapter.BodyImagesViewHolder> {

        Context context;
        ArrayList<String> images;
        LayoutInflater inflater;
        Fragment fragment;

        public BodyImagesAdapter(Context context, ArrayList<String> images, Fragment fragment) {
            this.context = context;
            this.images = images;
            inflater = LayoutInflater.from(context);
            this.fragment = fragment;
        }

        @Override
        public BodyImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.image_item, parent, false);
            return new BodyImagesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(BodyImagesViewHolder holder, int position) {
            final String uri = images.get(position);
            holder.ivImage.setImageURI(Uri.parse(images.get(position)));
            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePopFragment imagePopFragment = new ImagePopFragment();
                    Bundle args = new Bundle();
                    args.putString("uri", uri);
                    imagePopFragment.setArguments(args);
                    imagePopFragment.show(fragment.getChildFragmentManager(), "Image");
                }
            });

        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        static class BodyImagesViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivImage;

            public BodyImagesViewHolder(View itemView) {
                super(itemView);
                ivImage = (SimpleDraweeView) itemView.findViewById(R.id.ivImage);
            }
        }
    }
}
