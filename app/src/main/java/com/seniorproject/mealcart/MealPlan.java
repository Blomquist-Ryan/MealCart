package com.seniorproject.mealcart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MealPlan extends AppCompatActivity {
    private Calendar calendar;
    private TextView weekOf;
    private Calendar weekBeginning;
    private Button toShoppingCartButton;
    private Button toPantryButton;
    private Button toRecipeButton;
    private ImageButton nextWeekButton;
    private ImageButton previousWeekButton;
    private MealPlanRecyclerAdapter adapter;
    private Ingredient tempIngredient;
    RecyclerView mealPlanRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        calendar = Calendar.getInstance();

        setUI();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new MealPlanRecyclerAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mealPlanRecyclerView.setLayoutManager(layoutManager);

        mealPlanRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mealPlanRecyclerView.setAdapter(adapter);
        setAdapterButtonClicks();
    }

    private void setAdapterButtonClicks() {
        adapter.setOnItemClickListener(new MealPlanRecyclerAdapter.OnItemClickListener() {
            @Override
            public void make(int position) {
                }

            @Override
            public void plan(int position) {

            }
        });
    }
    
    private void setUI()
    {
        toShoppingCartButton = findViewById(R.id.toShoppingCart);
        toPantryButton = findViewById(R.id.toPantry);
        toRecipeButton = findViewById(R.id.toCookbook);
        nextWeekButton = findViewById(R.id.nextWeek);
        previousWeekButton = findViewById(R.id.previousWeek);
        mealPlanRecyclerView = findViewById(R.id.mealPlan);
        setOnClicksEvents();
        weekOf = findViewById(R.id.weekOf);
        setWeek();
    }

    private void setWeek() {
        calendar.add(Calendar.DAY_OF_MONTH, Calendar.SATURDAY - calendar.get(Calendar.DAY_OF_WEEK));
        String weekEnd = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - calendar.DAY_OF_WEEK);
        String weekBeginning = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        String week = weekBeginning + " - " + weekEnd;
        weekOf.setText(week);
    }

    private void setOnClicksEvents()
    {
        settoShoppingCartButtonOnClick();
        settoPantryButtonOnClick();
        settoRecipeButtonOnClick();
        setnextWeekButtonOnClick();
        setpreviousWeekButtonOnClick();
    }

    //region onClick Listeners
    private void setpreviousWeekButtonOnClick() {previousWeekButton.setOnClickListener(view ->previousWeek()); }

    private void setnextWeekButtonOnClick() {nextWeekButton.setOnClickListener(view -> nextWeek()); }

    private void settoRecipeButtonOnClick() {toRecipeButton.setOnClickListener(view ->openCookbook()); }

    private void settoPantryButtonOnClick() {toPantryButton.setOnClickListener(view -> openPantry()); }

    private void settoShoppingCartButtonOnClick() {toShoppingCartButton.setOnClickListener(view -> openShoppingCart()); }



    //endregion

    //region onClick Events
    private void previousWeek() {
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        setWeek();
    }

    private void nextWeek() {
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        setWeek();
    }

    private void openShoppingCart() {
        Intent intent = new Intent(this, ShoppingCart.class);
        startActivity(intent);
    }

    private void openPantry() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void openCookbook()
    {
        Intent intent = new Intent(this, Cookbook.class);
        startActivity(intent);
    }
    //endregion

    private void decreaseItem(Ingredient ingredient, ArrayList<Ingredient> list)
    {
        if (!itemExists(ingredient, list))
        {
            Toast.makeText(this, "Insufficient " + ingredient.getName() + " to make this meal", Toast.LENGTH_SHORT).show();
        }
        else
        {
            list.get(list.indexOf(tempIngredient)).decreaseIngredient(ingredient.getQty());
        }
    }

    private boolean itemExists(Ingredient ingredient, ArrayList<Ingredient> list) {
        for (Ingredient Item: list
        ) {
            if (Item.getName().equals(ingredient.getName()))
            {
                tempIngredient = Item;
                return true;
            }
        }
        return false;
    }
}