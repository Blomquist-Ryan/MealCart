package com.seniorproject.mealcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Cookbook extends AppCompatActivity  {
    private Button addRecipe;
    private Button toPantry;
    private Button toShoppingCart;
    private EditText searchBar;
    private RecyclerView recipeList;
    private ArrayList<Recipe> cookbook;
    private CookbookRecyclerAdapter adapter;
    private SaveData data;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookbook);
        data = new SaveData();
        cookbook = data.loadCookbook(this);
        recipeList = findViewById(R.id.recipeList);
        setUI();
        setEvents();
        setAdapter();
    }

    private void setUI() {
        addRecipe = findViewById(R.id.addToRecipeButton);
        toPantry = findViewById(R.id.toPantry);
        toShoppingCart = findViewById(R.id.toShoppingCart);
        searchBar = findViewById(R.id.searchBar);
    }

    private void filter(String searchCriteria)
    {
        ArrayList<Recipe> filteredRecipes = new ArrayList<Recipe>();
        for (Recipe recipe: cookbook)
        {
            if (recipe.getName().toLowerCase().contains(searchCriteria.toLowerCase()))
            {
                filteredRecipes.add(recipe);
            }
        }
        adapter.filter(filteredRecipes);
    }

    private void setEvents()
    {
        setAddRecipeButton();
        setToShoppingCartButton();
        setToPantryButton();
        setSearchEvent();
    }

    private void setAdapter()
    {
        adapter = new CookbookRecyclerAdapter(cookbook);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recipeList.setLayoutManager(layoutManager);

        recipeList.setItemAnimator(new DefaultItemAnimator());
        recipeList.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                cookbook.remove(viewHolder.getAdapterPosition());
                update();
            }
        }).attachToRecyclerView(recipeList);

        adapter.setOnItemClickListener(new CookbookRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position) {
                recipe = cookbook.get(position);
                openRecipe();
            }
        });
    }

    private void update()
    {
        adapter.notifyDataSetChanged();
        data.saveCookbook(this, cookbook);
    }

    private void setSearchEvent()
    {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    //region onclick events

    private void openShoppingCart() {
        update();
        Intent intent = new Intent(this, ShoppingCart.class);
        startActivity(intent);
    }
    private void openPantry() {
        update();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openRecipe()
    {
        update();
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    private void openCookbookAddition()
    {
        Intent intent = new Intent(this, CookbookAddition.class);
        startActivity(intent);
    }

    //endregion

    //region onclick Listeners

    private void setToShoppingCartButton()
    {
        toShoppingCart.setOnClickListener(view -> openShoppingCart());
    }
    private void setToPantryButton()
    {
        toPantry.setOnClickListener(view -> openPantry());
    }

    private void setAddRecipeButton()
    {
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                openCookbookAddition();
            }
        });
    }
    //endregion

}