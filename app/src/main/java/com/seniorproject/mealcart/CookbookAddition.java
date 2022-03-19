package com.seniorproject.mealcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CookbookAddition extends AppCompatActivity implements PantryAdditionModal.PantryAdditionDialogListener{
    private Button addIngredient;
    private Button cancel;
    private Button addToCookbook;
    private EditText directions;
    private ArrayList<Ingredient> ingredients;
    private RecyclerView recyclerView;
    private Recipe recipe;
    private EditText name;
    private Ingredient tempIngredient;
    private CookbookAdditionRecyclerAdapter adapter;
    private ArrayList<Recipe> cookbook;
    private SaveData data;
    private boolean isBeingEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookbook_addition);
        data = new SaveData();
        ingredients = new ArrayList<>();
        cookbook = data.loadCookbook(this);
        setUI();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new CookbookAdditionRecyclerAdapter(ingredients);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ingredients.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUI() {
        addIngredient = findViewById(R.id.addRecipeIngredientButton);
        cancel = findViewById(R.id.cancel);
        addToCookbook = findViewById(R.id.addRecipeButton);
        directions = findViewById(R.id.addDirectionText);
        recyclerView = findViewById(R.id.addRecipeIngredientList);
        name = findViewById(R.id.addRecipeTitle);
        setButtonOnClicks();
        Intent intent = getIntent();
        isBeingEdited = intent.getBooleanExtra("editRecipe", false);
        if(isBeingEdited)
        {
            preset();
        }
    }

    private void setButtonOnClicks()
    {
        setAddIngredientOnClick();
        setCancelOnClick();
        setAddToCookbookOnClick();
    }

    private void preset() {
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("Recipe");
        name.setText(recipe.getName());
        directions.setText(recipe.getDirection());
        ingredients = recipe.getIngredients();
    }

    @Override
    public void applyAddition(String name, float qty) {
        Ingredient newIngredient  = new Ingredient(name, qty);
        AddItem(newIngredient, ingredients);
        adapter.notifyDataSetChanged();
    }

    //region onclick listeners
    private void setAddToCookbookOnClick() {
        addToCookbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe();
            }
        });
    }

    private void setCancelOnClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCookbook();
            }
        });
    }

    private void setAddIngredientOnClick() {
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    //endregion

    //region on click events
    private void openCookbook() {
        Intent intent = new Intent(CookbookAddition.this, Cookbook.class);
        startActivity(intent);
    }

    public void openDialog() {
        PantryAdditionModal addition = new PantryAdditionModal();
        addition.show(getSupportFragmentManager(), "addtorecipe");
        adapter.notifyDataSetChanged();

    }

    public void addRecipe()
    {
        String recipeName = name.getText().toString();
        String directions = this.directions.getText().toString();
        if (!isDuplicateRecipe(recipeName))
        {
            cookbook.add(new Recipe(recipeName, ingredients, directions));
        }
        else
        {
            updateRecipe(recipeName, directions);
        }
        data.saveCookbook(this,cookbook);
        openCookbook();
    }
    //endregion

    private void updateRecipe(String recipeName, String directions)
    {
        cookbook.get(cookbook.indexOf(recipe)).setName(recipeName);
        cookbook.get(cookbook.indexOf(recipe)).setDirections(directions);
        cookbook.get(cookbook.indexOf(recipe)).setIngredients(ingredients);
    }

    private boolean isDuplicateRecipe(String name)
    {
        for (Recipe storedRecipe:cookbook)
        {
            if (name.equalsIgnoreCase(storedRecipe.getName()))
            {
                recipe = storedRecipe;
                return true;
            }

        }
        return false;
    }

    private void AddItem(Ingredient ingredient, ArrayList<Ingredient> list)
    {
        if (!isDuplicate(ingredient.getName(), list))
        {
            list.add(ingredient);
        }
        else
        {
            list.get(list.indexOf(tempIngredient)).increaseIngredient(ingredient.getQty());
        }
    }

    private boolean isDuplicate(String name, ArrayList<Ingredient> list) {
        for (Ingredient Item: list
        ) {
            if (Item.getName().equals(name))
            {
                tempIngredient = Item;
                return true;
            }
        }
        return false;
    }
}