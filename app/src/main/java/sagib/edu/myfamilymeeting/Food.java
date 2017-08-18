package sagib.edu.myfamilymeeting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sagib on 18/08/2017.
 */

public class Food implements Parcelable {
    String name;
    String recipe;

    public Food(String name, String recipe) {
        this.name = name;
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", recipe='" + recipe + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.recipe);
    }

    protected Food(Parcel in) {
        this.name = in.readString();
        this.recipe = in.readString();
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
