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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FoodFragment extends Fragment {

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
        String allRecipes = prefs.getString("allRecipes", "");
        Log.d("SagiBstart", allRecipes);
        String[] allRecipesArray = allRecipes.split("מתכון:");

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
        String lineSep = "\n";
        for (String s : allRecipesArray) {
            String[] split = s.split("\\r?\\n");
            String name = "";
            StringBuilder data = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                if (i == 1) {
                    name = split[i];
                } else {
                    data.append(split[i]);
                    data.append(lineSep);
                }
            }
            String recipe = data.toString();
            Recipe recipe1 = new Recipe(name, recipe);
            recipes.add(recipe1);
            Log.d("SagiB", recipe1.toString());
        }

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
        tvOpening.setText(openMenu);
        FoodAdapter openingAdapter = new FoodAdapter(firstsArray, getContext(), recipes, this);
        rvOpening.setAdapter(openingAdapter);
        rvOpening.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter firstsAdapter = new FoodAdapter(secondsArray, getContext(), recipes, this);
        rvFirsts.setAdapter(firstsAdapter);
        rvFirsts.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter primarysAdapter = new FoodAdapter(primarysArray, getContext(), recipes, this);
        rvPrimarys.setAdapter(primarysAdapter);
        rvPrimarys.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter toppingsAdapter = new FoodAdapter(topingsArray, getContext(), recipes, this);
        rvToppings.setAdapter(toppingsAdapter);
        rvToppings.setLayoutManager(new LinearLayoutManager(getContext()));
        FoodAdapter lastfoodAdapter = new FoodAdapter(lastsArray, getContext(), recipes, this);
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
        Bundle args = new Bundle();
        Fragment fragment;

        public FoodAdapter(ArrayList<Food> food, Context context, ArrayList<Recipe> recipes, Fragment fragment) {
            this.food = food;
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.recipes = recipes;
            this.fragment = fragment;
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
                    if (recipe.getFoodName().equals(thisFood.getName())) {
                        thisFood.setRecipe(recipe.getRecipe());
                        args.clear();
                        args.putParcelable("Recipe", recipe);

                    }
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (thisFood.getRecipe() != null) {
                        RecipeDialogFragment recipeDialogFragment = new RecipeDialogFragment();
                        recipeDialogFragment.setArguments(args);
                        recipeDialogFragment.show(fragment.getChildFragmentManager(), "Recipe");

                    }
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
