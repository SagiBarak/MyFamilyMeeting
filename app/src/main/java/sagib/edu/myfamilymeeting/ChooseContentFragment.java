package sagib.edu.myfamilymeeting;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ChooseContentFragment extends BottomSheetDialogFragment {


    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.tvChooseDate)
    TextView tvChooseDate;
    Unbinder unbinder;
    SharedPreferences dataPrefs;
    SharedPreferences prefs;
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    Gson gson = new Gson();
    @BindView(R.id.btnChoose)
    BootstrapButton btnChoose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_content, container, false);
        unbinder = ButterKnife.bind(this, v);
        dataPrefs = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        prefs = getContext().getSharedPreferences("Values", Context.MODE_PRIVATE);
        dates = gson.fromJson(dataPrefs.getString("currentParsedDates", ""), new TypeToken<ArrayList<String>>() {
        }.getType());
        Collections.reverse(dates);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(R.layout.dropdownitem);
        spinner.setAdapter(adapter);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getDataByDate(final String date) {
        if (date == null) {
            return;
        }
        FirebaseDatabase.getInstance().getReference("Data").child(date).child("text").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String adjusted = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "body":
                            String body = snapshot.getValue(String.class);
                            if (body != null)
                                adjusted = body.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("body", adjusted).commit();
                            break;
                        case "endingText":
                            String endingText = snapshot.getValue(String.class);
                            if (endingText != null)
                                adjusted = endingText.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("endingText", adjusted).commit();
                            break;
                        case "firstFood":
                            String firstFood = snapshot.getValue(String.class);
                            if (firstFood != null)
                                adjusted = firstFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("firstFood", adjusted).commit();
                            break;
                        case "header":
                            String header = snapshot.getValue(String.class);
                            if (header != null)
                                adjusted = header.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("header", adjusted).commit();
                            break;
                        case "lastFood":
                            String lastFood = snapshot.getValue(String.class);
                            if (lastFood != null)
                                adjusted = lastFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("lastFood", adjusted).commit();
                            break;
                        case "openMenu":
                            String openMenu = snapshot.getValue(String.class);
                            if (openMenu != null)
                                adjusted = openMenu.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("openMenu", adjusted).commit();
                            break;
                        case "opening":
                            String opening = snapshot.getValue(String.class);
                            if (opening != null)
                                adjusted = opening.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("opening", adjusted).commit();
                            break;
                        case "primaryFood":
                            String primaryFood = snapshot.getValue(String.class);
                            if (primaryFood != null)
                                adjusted = primaryFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("primaryFood", adjusted).commit();
                            break;
                        case "quiz":
                            String quiz = snapshot.getValue(String.class);
                            if (quiz != null)
                                adjusted = quiz.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("quiz", adjusted).commit();
                            break;
                        case "secondFood":
                            String secondFood = snapshot.getValue(String.class);
                            if (secondFood != null)
                                adjusted = secondFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("secondFood", adjusted).commit();
                            break;
                        case "textBeforeMenu":
                            String textBeforeMenu = snapshot.getValue(String.class);
                            if (textBeforeMenu != null)
                                adjusted = textBeforeMenu.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("textBeforeMenu", adjusted).commit();
                            break;
                        case "textDate":
                            String textDate = snapshot.getValue(String.class);
                            if (textDate != null)
                                adjusted = textDate.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("textDate", adjusted).commit();
                            break;
                        case "topingsFood":
                            String topingsFood = snapshot.getValue(String.class);
                            if (topingsFood != null)
                                adjusted = topingsFood.replaceAll("(?m)^[ \t]*\r?\n", "");
                            prefs.edit().putString("topingsFood", adjusted).commit();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Data").child(date).child("imageCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    switch (snapshot.getKey()) {
                        case "bodyImages":
                            String bodyImages = snapshot.getValue(String.class);
                            prefs.edit().putString("bodyImages", bodyImages).commit();
                            break;
                        case "foodImages":
                            String foodImages = snapshot.getValue(String.class);
                            prefs.edit().putString("foodImages", foodImages).commit();
                            break;
                        case "openImages":
                            String openImages = snapshot.getValue(String.class);
                            prefs.edit().putString("openImages", openImages).commit();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Data").child(date).child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    images.add(snapshot.getValue(String.class));
                    prefs.edit().remove("allImages").commit();
                    prefs.edit().putString("allImages", gson.toJson(images)).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btnChoose)
    public void onBtnChooseClicked() {
        getDataByDate(spinner.getSelectedItem().toString());
        Handler h = new Handler();
        spinner.setVisibility(View.GONE);
        tvChooseDate.setVisibility(View.GONE);
        btnChoose.setText("טוען...");
        btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
        btnChoose.setClickable(false);
        final FragmentManager fragmentManager = this.getFragmentManager();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                dismiss();
                fragmentManager.beginTransaction().replace(R.id.content, new WelcomeFragment()).commit();
            }
        };
        h.postDelayed(r, 2000);
    }
}
