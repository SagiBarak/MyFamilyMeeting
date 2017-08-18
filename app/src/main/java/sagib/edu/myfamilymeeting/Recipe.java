package sagib.edu.myfamilymeeting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sagib on 18/08/2017.
 */

public class Recipe implements Parcelable {
    String foodName;
    String recipe;

    public Recipe(String foodName, String recipe) {
        this.foodName = foodName;
        this.recipe = recipe;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "foodName='" + foodName + '\'' +
                ", recipe='" + recipe + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foodName);
        dest.writeString(this.recipe);
    }

    protected Recipe(Parcel in) {
        this.foodName = in.readString();
        this.recipe = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
