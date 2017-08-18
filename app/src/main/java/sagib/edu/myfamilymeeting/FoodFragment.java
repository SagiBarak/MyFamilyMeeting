package sagib.edu.myfamilymeeting;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
    @BindView(R.id.tvFirstTitle)
    TextView tvFirstTitle;
    @BindView(R.id.tvPrimaryTitle)
    TextView tvPrimaryTitle;
    @BindView(R.id.tvToppingsTitle)
    TextView tvToppingsTitle;
    @BindView(R.id.tvLastFoodTitle)
    TextView tvLastFoodTitle;
    Unbinder unbinder;
    SharedPreferences prefs;
    @BindView(R.id.rvFirsts)
    RecyclerView rvFirsts;
    @BindView(R.id.rvOpening)
    RecyclerView rvOpening;
    @BindView(R.id.rvPrimarys)
    RecyclerView rvPrimarys;
    @BindView(R.id.rvToppings)
    RecyclerView rvToppings;
    @BindView(R.id.rvLastFood)
    RecyclerView rvLastFood;

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

        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("חמוצים סינים", "ערבב בעדינות..."));

        String[] firsts = firstFood.split("\\r?\\n");
        String[] seconds = secondFood.split("\\r?\\n");
        String[] primarys = primaryFood.split("\\r?\\n");
        String[] topings = topingsFood.split("\\r?\\n");
        String[] lasts = lastFood.split("\\r?\\n");
        ArrayList<Food> firstsArray = new ArrayList<>();
        ArrayList<Food> secondsArray = new ArrayList<>();
        ArrayList<Food> primarysArray = new ArrayList<>();
        ArrayList<Food> topingsArray = new ArrayList<>();
        ArrayList<Food> lastsArray = new ArrayList<>();
        for (String first : firsts) {
            first = first.replaceAll("\\s+", " ");
            first = first.trim();
            Food food = new Food(first, null);
            firstsArray.add(food);
        }
        for (String second : seconds) {
            second = second.replaceAll("\\s+", " ");
            second = second.trim();
            Food food = new Food(second, null);
            secondsArray.add(food);
        }
        for (String primary : primarys) {
            primary = primary.replaceAll("\\s+", " ");
            primary = primary.trim();
            Food food = new Food(primary, null);
            primarysArray.add(food);
        }
        for (String toping : topings) {
            toping = toping.replaceAll("\\s+", " ");
            toping = toping.trim();
            Food food = new Food(toping, null);
            topingsArray.add(food);
        }
        for (String last : lasts) {
            last = last.replaceAll("\\s+", " ");
            last = last.trim();
            Food food = new Food(last, null);
            lastsArray.add(food);
        }

        FoodAdapter openingAdapter = new FoodAdapter(firstsArray, getContext(), recipes);
        rvOpening.setAdapter(openingAdapter);
        rvOpening.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter firstsAdapter = new FoodAdapter(secondsArray, getContext(), recipes);
        rvFirsts.setAdapter(firstsAdapter);
        rvFirsts.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter primarysAdapter = new FoodAdapter(primarysArray, getContext(), recipes);
        rvPrimarys.setAdapter(primarysAdapter);
        rvPrimarys.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter toppingsAdapter = new FoodAdapter(topingsArray, getContext(), recipes);
        rvToppings.setAdapter(toppingsAdapter);
        rvToppings.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter lastfoodAdapter = new FoodAdapter(lastsArray, getContext(), recipes);
        rvLastFood.setAdapter(lastfoodAdapter);
        rvLastFood.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
        ArrayList<Food> food;
        Context context;
        LayoutInflater inflater;
        ArrayList<Recipe> recipes;

        public FoodAdapter(ArrayList<Food> food, Context context, ArrayList<Recipe> recipes) {
            this.food = food;
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.recipes = recipes;
        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.food_item, parent, false);
            return new FoodViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final FoodViewHolder holder, int position) {
            final Food thisFood = food.get(position);
            holder.tvFoodItem.setText(thisFood.getName());
            if (recipes != null) {
                for (Recipe recipe : recipes) {
                    Log.d("SagiB recipe", recipe.getFoodName());
                    Log.d("SagiB recipe", thisFood.getName());
                    if (recipe.getFoodName().equals(thisFood.getName())) {
                        thisFood.setRecipe(recipe.getRecipe());
                    }
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (thisFood.getRecipe() != null)
                        Toast.makeText(context, thisFood.getRecipe(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return food.size();
        }

        static class FoodViewHolder extends RecyclerView.ViewHolder {

            TextView tvFoodItem;

            public FoodViewHolder(View itemView) {
                super(itemView);
                tvFoodItem = (TextView) itemView.findViewById(R.id.tvFoodItem);
            }
        }
    }
}
