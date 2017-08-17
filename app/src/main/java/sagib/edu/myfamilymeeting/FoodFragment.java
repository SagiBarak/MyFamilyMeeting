package sagib.edu.myfamilymeeting;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FoodFragment extends Fragment {


    @BindView(R.id.tvMenuTitle)
    TextView tvMenuTitle;
    @BindView(R.id.tvOpening)
    TextView tvOpening;
    @BindView(R.id.tvOpeningTitle)
    TextView tvOpeningTitle;
    @BindView(R.id.tvFoodOpening)
    TextView tvFoodOpening;
    @BindView(R.id.tvFirstTitle)
    TextView tvFirstTitle;
    @BindView(R.id.tvFoodFirst)
    TextView tvFoodFirst;
    @BindView(R.id.tvPrimaryTitle)
    TextView tvPrimaryTitle;
    @BindView(R.id.tvFoodPrimary)
    TextView tvFoodPrimary;
    @BindView(R.id.tvToppingsTitle)
    TextView tvToppingsTitle;
    @BindView(R.id.tvFoodToppings)
    TextView tvFoodToppings;
    @BindView(R.id.tvLastFoodTitle)
    TextView tvLastFoodTitle;
    @BindView(R.id.tvLastFoodFood)
    TextView tvLastFoodFood;
    Unbinder unbinder;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        unbinder = ButterKnife.bind(this, v);
        prefs = getContext().getSharedPreferences("Values", Context.MODE_PRIVATE);

        String openMenu = prefs.getString("openMenu", "").replace("  ", " ");
        while (openMenu.contains("  ")) {
            openMenu = openMenu.replace("  ", " ");
        }
        String firstFood = prefs.getString("firstFood", "").replace("  ", " ");
        while (firstFood.contains("  ")) {
            firstFood = firstFood.replace("  ", " ");
        }
        String secondFood = prefs.getString("secondFood", "").replace("  ", " ");
        while (secondFood.contains("  ")) {
            secondFood = secondFood.replace("  ", " ");
        }
        String primaryFood = prefs.getString("primaryFood", "").replace("  ", " ");
        while (primaryFood.contains("  ")) {
            primaryFood = primaryFood.replace("  ", " ");
        }
        String topingsFood = prefs.getString("topingsFood", "").replace("  ", " ");
        while (topingsFood.contains("  ")) {
            topingsFood = topingsFood.replace("  ", " ");
        }
        String lastFood = prefs.getString("lastFood", "").replace("  ", " ");
        while (lastFood.contains("  ")) {
            lastFood = lastFood.replace("  ", " ");
        }

        tvOpening.setText(openMenu);
        tvFoodOpening.setText(firstFood);
        tvFoodFirst.setText(secondFood);
        tvFoodPrimary.setText(primaryFood);
        tvFoodToppings.setText(topingsFood);
        tvLastFoodFood.setText(lastFood);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
