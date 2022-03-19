package com.seniorproject.mealcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    private Button toPantry;
    private Button toShoppingCart;
    private Button toCookbook;
    private Button editRecipe;
    private Button addToShoppingCart;
    private Button makeMeal;
    private EditText modifier;
    private ArrayList<Ingredient> pantry;
    private ArrayList<Ingredient> shoppingCart;
    private Ingredient tempIngredient;
    private RecyclerView recipeRecyclerView;
    private RecipeRecyclerAdapter adapter;
    private Recipe recipe;
    private SaveData data;
    private float mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        setUI();
        setButtonOnClicks();
        setAdapter();

    }

    private void setAdapter() {
        adapter = new RecipeRecyclerAdapter(recipe.getIngredients(), pantry);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(adapter);
    }

    private void setUI() {
        initialSetup();

        TextView title = findViewById(R.id.recipeTitle);
        title.setText(recipe.getName());

        TextView direction = findViewById(R.id.directionText);
        direction.setText(recipe.getDirection());
        recipeRecyclerView = findViewById(R.id.recipeList);

        toPantry = findViewById(R.id.toPantry);
        toShoppingCart = findViewById(R.id.toShoppingCart);
        toCookbook = findViewById(R.id.toCookbook);
        editRecipe = findViewById(R.id.editRecipe);
        addToShoppingCart = findViewById(R.id.addToShoppingCartButton);
        makeMeal = findViewById(R.id.makeMealButton);
        modifier = findViewById(R.id.modifier);
    }

    private void initialSetup() {
        data = new SaveData();
        pantry = data.loadPantry(this);
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("Recipe");
        shoppingCart = data.loadShoppingCart(this);
        mod = 1;
    }

    private void setButtonOnClicks() {
        setToPantryButton();;
        setToShoppingCartButton();
        setToCookbook();
        setEditRecipeButton();
        setAddToShoppingCartButton();
        setMakeMealButton();
        setModifierChanges();
    }


    //region onclick events

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
    private void addtoCart() {

        for ( Ingredient ingredient : adapter.selectedItems() )
        {
            ingredient.modifyQty(mod);
            AddItem(ingredient, shoppingCart);
        }
        data.saveShoppingCart(this, shoppingCart);
        Toast.makeText(this, "ingredients added to shopping cart", Toast.LENGTH_SHORT).show();
    }

    private void editRecipe() {
        Intent intent = new Intent(RecipeActivity.this, CookbookAddition.class);
        intent.putExtra("editRecipe", true);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    private void decreaseFromPantry() {
        for (Ingredient ingredient: recipe.getIngredients() )
        {
            ingredient.modifyQty(mod);
            decreaseItem(ingredient, pantry);
        }
        data.savePantry(this, pantry);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Ingredients removed from the Pantry", Toast.LENGTH_SHORT).show();
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
    private void setToCookbook()
    {
        toCookbook.setOnClickListener(view -> openCookbook());
    }

    private void setAddToShoppingCartButton() {addToShoppingCart.setOnClickListener(view -> addtoCart()); }

    private void setEditRecipeButton() {
        editRecipe.setOnClickListener(view -> editRecipe());
    }

    private void setMakeMealButton() { makeMeal.setOnClickListener(view -> decreaseFromPantry()); }

    private void setModifierChanges() { modifier.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString()) || s.toString().equalsIgnoreCase("."))
            {
                mod = 1;
            }
            else
            {
                mod = Float.parseFloat(s.toString());
            }

                 if(mod < 0)
                 {
                     mod = 1;
                 }
                 adapter.modifier = mod;
            adapter.notifyDataSetChanged();

        }
    });
    }
    //endregion

    private void AddItem(Ingredient ingredient, ArrayList<Ingredient> list)
    {
        if (!itemExists(ingredient, list))
        {
            list.add(ingredient);
        }
        else
        {
            list.get(list.indexOf(tempIngredient)).increaseIngredient((int)ingredient.getQty());
        }
    }

    private void decreaseItem(Ingredient ingredient, ArrayList<Ingredient> list)
    {
        if (!itemExists(ingredient, list))
        {
            Toast.makeText(this, "Insufficient " + ingredient.getName() + " to make this meal", Toast.LENGTH_SHORT).show();
        }
        else
        {
            list.get(list.indexOf(tempIngredient)).decreaseIngredient((int)ingredient.getQty());
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