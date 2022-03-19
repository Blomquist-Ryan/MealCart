package com.seniorproject.mealcart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShoppingCart extends AppCompatActivity implements ShoppingCartAdditionModal.ShoppingCartAdditionDialogListener{
    private static final String SHOPPING_CART = "shopping cart";
    private ArrayList<Ingredient> shoppingCart;
    private ArrayList<Ingredient> pantry;

    private Ingredient tempIngredient;
    private Button toPantryButton;
    private Button addItemButton;
    private Button purchaseButton;
    private Button clearButton;
    private Button toCookbook;
    private RecyclerView recyclerView;
    private ShoppingCartRecyclerAdapter adapter;
    private SaveData data = new SaveData();
    //TODO: fix button placement so it looks uniform for purchase and clear buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        shoppingCart = data.loadShoppingCart(this);
        pantry = data.loadPantry(this);


        setUI();
        setButtonOnClicks();
        recyclerView = findViewById(R.id.shoppingCart);
        setAdapter();

    }

    private void setUI() {
        toPantryButton = findViewById(R.id.toPantry);
        clearButton = findViewById(R.id.clearButton);
        toCookbook = findViewById(R.id.toCookbook);
        purchaseButton = findViewById(R.id.Purchase);
        addItemButton = findViewById(R.id.addToShoppingCartButton);
    }

    private void setButtonOnClicks() {
        setAddItemButton();
        setToPantryButton();
        setToCookbookButton();
        setClearButton();
        setPurchaseButton();
    }



    private void setAdapter() {
        adapter = new ShoppingCartRecyclerAdapter(shoppingCart);
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
                shoppingCart.remove(viewHolder.getAdapterPosition());
                update();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ShoppingCartRecyclerAdapter.OnItemClickListener() {
            @Override
            public void increase(int position) {
                shoppingCart.get(position).increaseIngredient(1);
                update();
            }

            @Override
            public void decrease(int position) {
                shoppingCart.get(position).decreaseIngredient(1);
                update();
            }
        });
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

    @Override
    public void applyAddition(String name, float qty) {
        Ingredient ingredient = new Ingredient(name, qty);
        AddItem(ingredient, shoppingCart);
        update();
    }

    public void removeItemsInShoppingCart(ArrayList<Ingredient> itemsBought)
    {
        ArrayList<Ingredient> newShoppingCart = new ArrayList<>();

        for (Ingredient removeItem:itemsBought)
        {
            for (Ingredient cartItem:shoppingCart)
            {
                if(removeItem.getName().equals(cartItem.getName()) && !newShoppingCart.contains(cartItem))
                {
                    newShoppingCart.add(cartItem);
                }
            }
        }
        shoppingCart.removeAll(newShoppingCart);
        update();
    }

    private void update()
    {
        adapter.notifyDataSetChanged();
        adapter.update(shoppingCart);
        data.saveShoppingCart(this, shoppingCart);
    }

    private void addItemsToPantry(ArrayList<Ingredient> itemsToAdd)
    {
        for (Ingredient ingredient :itemsToAdd
             ) {
            AddItem(ingredient, pantry);

        }
        data.savePantry(this, pantry);
    }

    //region OnClick Listeners
    private void setClearButton() {
        clearButton.setOnClickListener(v -> clearShoppingCart());
    }

    private void setToPantryButton() {
        toPantryButton.setOnClickListener(view -> {
            update();
            openPantry();
        });
    }

    private void setToCookbookButton() {
        toCookbook.setOnClickListener(view -> openCookbook());
    }

    private void setPurchaseButton() {
        purchaseButton.setOnClickListener(view -> purchase());
    }

    private void setAddItemButton() {
        addItemButton.setOnClickListener(view -> openDialog());
    }
    //endregion

    //region OnClick Events
    public void openDialog() {
        ShoppingCartAdditionModal addition = new ShoppingCartAdditionModal();
        addition.show(getSupportFragmentManager(), "addtoShoppingCart");
    }

    public void openPantry()
    {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openCookbook()
    {
        Intent intent =  new Intent(this, Cookbook.class);
        startActivity(intent);
    }

    private void purchase() {
        ArrayList<Ingredient> itemsBought = adapter.getItemAdditions();
        removeItemsInShoppingCart(itemsBought);
        addItemsToPantry(itemsBought);
        adapter.clearTemporaryList();
        update();
    }

    private void clearShoppingCart() {
        adapter.checkboxCheckStates.clear();
        shoppingCart.clear();
        update();
    }
    //endregion
}