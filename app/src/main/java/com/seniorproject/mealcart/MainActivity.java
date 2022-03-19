package com.seniorproject.mealcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PantryAdditionModal.PantryAdditionDialogListener {
    private static final String PANTRY = "pantry";
    private ArrayList<Ingredient> pantryItems;
    private Ingredient tempIngredient;
    private RecyclerView pantryRecyclerView;
    private Button addToPantryButton;
    private Button toShoppingCartButton;
    private Button toCookbook;
    //private Button toMealPlan;
    private pantryRecyclerAdapter adapter;
    private SaveData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
        setButtonOnClicks();
        setAdapter();
    }

    private void setUI() {
        addToPantryButton = findViewById(R.id.addToPantryButton);
        toShoppingCartButton = findViewById(R.id.toShoppingCart);
        toCookbook = findViewById(R.id.toCookbook);
        //toMealPlan = findViewById(R.id.toMealPlan);
        pantryRecyclerView = findViewById(R.id.pantryList);
        data = new SaveData();
        pantryItems = data.loadPantry(this);
    }

    private void setButtonOnClicks() {
        setToCookbookOnClick();
        setToShoppingCartOnClick();
        setAddToPantryOnClick();
        //setToMealPlan();
    }

    private void setAdapter() {
        adapter = new pantryRecyclerAdapter(pantryItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        pantryRecyclerView.setLayoutManager(layoutManager);

        pantryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pantryRecyclerView.setAdapter(adapter);

        setSwipeToRemove();
        setAdapterButtonClicks();
    }

    private void setAdapterButtonClicks() {
        adapter.setOnItemClickListener(new pantryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void increase(int position) {
                pantryItems.get(position).increaseIngredient(1);
                update(pantryItems);
            }

            @Override
            public void decrease(int position) {
                pantryItems.get(position).decreaseIngredient(1);
                update(pantryItems);
            }
        });
    }

    private void setSwipeToRemove() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                pantryItems.remove(viewHolder.getAdapterPosition());
                update(pantryItems);
            }
        }).attachToRecyclerView(pantryRecyclerView);
    }

    public void addMeal(ArrayList<Ingredient> recipe)
    {
        for (Ingredient ingredient:recipe)
        {
            addIngredient(ingredient);
        }
        update(pantryItems);
    }

    public ArrayList<Ingredient> getPantry()
    {
        return pantryItems;
    }

    public void makeMeal()
    {
        adapter.notifyDataSetChanged();
    }

    public void addIngredient(Ingredient ingredient)
    {
        if (!isDuplicate(ingredient.getName()))
        {
            pantryItems.add(ingredient);
        }
        else
        {
            pantryItems.get(pantryItems.indexOf(tempIngredient)).increaseIngredient((int)ingredient.getQty());
        }
    }

    @Override
    public void applyAddition(String name, float qty) {
        Ingredient newIngredient = new Ingredient(name, qty);
        addIngredient(newIngredient);
        update(pantryItems);
    }



    private void update(ArrayList<Ingredient> listToUpdate)
    {

        adapter.notifyDataSetChanged();
        data.savePantry(this, listToUpdate);
    }

    private boolean isDuplicate(String ingredientName)
    {
        for (Ingredient pantryItem: pantryItems
             ) {
            if (pantryItem.getName().equals(ingredientName))
            {
                tempIngredient = pantryItem;
                return true;
            }
        }
        return false;
    }

    //region onclick listeners

    private void setAddToPantryOnClick() {
        addToPantryButton.setOnClickListener(view -> openDialog());
    }
    private void setToShoppingCartOnClick() {
        toShoppingCartButton.setOnClickListener(view -> openShoppingCart());
    }

    private void setToCookbookOnClick() {
        toCookbook.setOnClickListener(view -> openCookbook());
    }

    //private void setToMealPlan() {toMealPlan.setOnClickListener(view -> openMealPlan());}

    //endregion


    //region onclick events

    private void openShoppingCart() {
        Intent intent = new Intent(this, ShoppingCart.class);
        startActivity(intent);
    }
    private void openCookbook() {
        Intent intent = new Intent(this, Cookbook.class);
        startActivity(intent);
    }

    public void openDialog() {
        PantryAdditionModal addition = new PantryAdditionModal();
        addition.show(getSupportFragmentManager(), "addtopantry");
    }

//    private void openMealPlan() {
//        Intent intent = new Intent(this, MealPlan.class);
//        startActivity(intent);
//    }

    //endregion
}