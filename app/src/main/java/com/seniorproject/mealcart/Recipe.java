package com.seniorproject.mealcart;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    private String name;
    private String directions;
    private ArrayList<Ingredient> ingredients;
    private float modifier;

    public Recipe(String name, ArrayList<Ingredient> ingredients, String directions)
    {
        this.name = name;
        this.directions = directions;
        this.ingredients = ingredients;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDirections(String directions) { this.directions = directions; }

    public void setIngredients(ArrayList<Ingredient> ingredients) { this.ingredients = ingredients; }

    public void setModifier(float modifier) { this.modifier = modifier; }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        directions = in.readString();
        modifier = in.readInt();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(directions);
        dest.writeFloat(modifier);
        dest.writeTypedList(ingredients);
    }

    public String getDirection() {
        return directions;
    }
}
