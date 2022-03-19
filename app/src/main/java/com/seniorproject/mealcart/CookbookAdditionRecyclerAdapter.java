package com.seniorproject.mealcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CookbookAdditionRecyclerAdapter extends RecyclerView.Adapter<CookbookAdditionRecyclerAdapter.MyViewHolder>{

    private ArrayList<Ingredient> ingredients;

    public CookbookAdditionRecyclerAdapter(ArrayList<Ingredient> ingredients)
    {
        this.ingredients= ingredients;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView ingredientName;
        private EditText ingredientqty;

        public MyViewHolder(final View view) {
            super(view);
            ingredientName = view.findViewById(R.id.recipeAdditionIngredientName);
            ingredientqty = view.findViewById(R.id.recipeAdditionIngredientqty);
        }
    }

    @NonNull
    @Override
    public CookbookAdditionRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_addition_item, parent, false);
        return new CookbookAdditionRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = ingredients.get(position).getName();
        String qty =  String.valueOf(ingredients.get(position).getQty());
        holder.ingredientName.setText(name);
        holder.ingredientqty.setText(qty);
    }

    @Override
    public int getItemCount()
    {
        return  ingredients.size();
    }
}
