package com.seniorproject.mealcart;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SaveData{//} extends AppCompatActivity {
    private static final String PANTRY = "pantry";
    private static final String SHOPPING_CART = "shopping cart";
    private static final String COOKBOOK = "cookbook";
    private Gson gson = new Gson();
    private ArrayList<Ingredient> pantry;
    private ArrayList<Ingredient> shoppingCart;
    private ArrayList<Recipe> cookbook;
    private String json;


    public SaveData() {
        shoppingCart = new ArrayList();
        pantry = new ArrayList();
    };


    //region saves
    public void saveShoppingCart(Context context, ArrayList<Ingredient> listToSave)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHOPPING_CART, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        removeEmptyIngredients(listToSave);

        json = gson.toJson(listToSave);
        editor.putString(SHOPPING_CART, json);
        editor.apply();
    }
    public void savePantry(Context context, ArrayList<Ingredient> listToSave)
    {


        SharedPreferences preferences = context.getSharedPreferences(PANTRY, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        removeEmptyIngredients(listToSave);

        json = gson.toJson(listToSave);
        editor.putString(PANTRY, json);
        editor.apply();
    }

    public void saveCookbook(Context context, ArrayList<Recipe> listToSave)
    {
        SharedPreferences preferences = context.getSharedPreferences(COOKBOOK, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        json = gson.toJson(listToSave);
        editor.putString(COOKBOOK, json);
        editor.apply();
    }

    //endregion
    //region loads

    public ArrayList<Ingredient> loadShoppingCart(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHOPPING_CART, MODE_PRIVATE);
        json = preferences.getString(SHOPPING_CART, null);
        Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        shoppingCart = gson.fromJson(json, type);

        if(isNullOrEmpty(shoppingCart))
        {
           return new ArrayList<>();
        }
        return shoppingCart;
    }
    public ArrayList<Ingredient> loadPantry(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(PANTRY, MODE_PRIVATE);
        json = preferences.getString(PANTRY, null);
        Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        pantry = gson.fromJson(json, type);

        if(isNullOrEmpty(pantry))
        {
            return new ArrayList<>();
        }
        return pantry;
    }

    public ArrayList<Recipe> loadCookbook(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(COOKBOOK, MODE_PRIVATE);
        json = preferences.getString(COOKBOOK, null);
        Type type = new TypeToken<ArrayList<Recipe>>() {}.getType();
        cookbook = gson.fromJson(json, type);

        if(isCookbookNullOrEmpty(cookbook))
        {
            return new ArrayList<>();
        }
        return cookbook;
    }

    //endregion
    public void addItemsToPantry(Context context, ArrayList<Ingredient> itemsToAdd)
    {
        SharedPreferences preferences = context.getSharedPreferences(PANTRY, MODE_PRIVATE);
        json = preferences.getString(PANTRY, null);
        Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        pantry = gson.fromJson(json, type);
        pantry.addAll(itemsToAdd);

        SharedPreferences.Editor editor = preferences.edit();

        json = gson.toJson(pantry);
        editor.putString(PANTRY, json);
        editor.apply();

    }

    public void addItemsToShoppingCart(Context context, ArrayList<Ingredient> itemsToAdd)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHOPPING_CART, MODE_PRIVATE);
        json = preferences.getString(SHOPPING_CART, null);
        Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        shoppingCart = gson.fromJson(json, type);
        shoppingCart.addAll(itemsToAdd);

        SharedPreferences.Editor editor = preferences.edit();

        json = gson.toJson(shoppingCart);
        editor.putString(SHOPPING_CART, json);
        editor.apply();

    }

    private boolean isNullOrEmpty(ArrayList<Ingredient> list)
    {
        if(list == null)
        {
            return true;
        }
        else if(list.isEmpty())
        {
            return true;
        }
        return false;
    }

    private boolean isCookbookNullOrEmpty(ArrayList<Recipe> list)
    {
        if(list == null)
        {
            return true;
        }
        else if(list.isEmpty())
        {
            return true;
        }
        return false;
    }

    private void removeEmptyIngredients(ArrayList<Ingredient> listToSave) {
        ArrayList<Ingredient> removalList = new ArrayList<>();
        for (Ingredient ingredient: listToSave
        ) {
            if (ingredient.getQty() <= 0)
            {
                removalList.add(ingredient);
            }
        }
        listToSave.removeAll(removalList);
    }
}
