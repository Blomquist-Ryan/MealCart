package com.seniorproject.mealcart;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private String name;
    private float qty;
   // private boolean selected;

    public Ingredient(String ingredient, float qty)//, boolean selected)
    {
        setName(ingredient);
        setQty(qty);
        //setSelected(selected);
    }

   // public boolean isSelected()
//    {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) { this.selected = selected; }

    protected Ingredient(Parcel in) {
        name = in.readString();
        qty = in.readFloat();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public void modifyQty(float qty){this.qty = this.qty * qty;}

    public void decreaseIngredient(float qtyToRemove) { this.qty -= qtyToRemove; }

    public void increaseIngredient(float qtyToAdd) { this.qty += qtyToAdd; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeFloat(this.qty);

    }
}
