package sagib.edu.myfamilymeeting;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeDialogFragment extends DialogFragment {


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvRecipe)
    TextView tvRecipe;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_dialog, container, false);
        tvRecipe = (TextView) v.findViewById(R.id.tvRecipe);
        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        Recipe recipe = getArguments().getParcelable("Recipe");
        if (recipe != null) {
            tvTitle.setText(recipe.getFoodName());
            tvRecipe.setText(recipe.getRecipe());
        }
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
