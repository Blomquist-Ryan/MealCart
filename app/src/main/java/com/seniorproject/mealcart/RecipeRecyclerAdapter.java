package com.seniorproject.mealcart;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.seniorproject.mealcart.CookbookRecyclerAdapter.MyViewHolder;

import java.util.ArrayList;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.MyViewHolder>{

    private ArrayList<Ingredient> pantry;
    private ArrayList<Ingredient> tempList;
    public float modifier;

    public interface OnItemClickListener{
        void mod(float modifier);
    }
    public void setOnItemClickListener(RecipeRecyclerAdapter.OnItemClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    private RecipeRecyclerAdapter.OnItemClickListener clickListener;

    private ArrayList<Ingredient> recipe;

    public RecipeRecyclerAdapter(ArrayList<Ingredient> recipe, ArrayList<Ingredient> pantry)
    {
        this.pantry = pantry;
        this.recipe = recipe;
        tempList = new ArrayList<>();
        modifier = 1.00f;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView recipeIngredientQty;
        private CheckBox recipeIngredient;

        public MyViewHolder(final View view, RecipeRecyclerAdapter.OnItemClickListener listener) {
            super(view);
            recipeIngredient = view.findViewById(R.id.recipeIngredients);
            recipeIngredientQty = view.findViewById(R.id.recipeIngredientQty);
        }
    }

    @NonNull
    @Override
    public RecipeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_item, parent, false);
        return new RecipeRecyclerAdapter.MyViewHolder(recipeView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        String qty = String.valueOf(recipe.get(position).getQty());
        float quantity = Float.parseFloat(qty) * modifier;
        qty = String.valueOf(quantity);
        Ingredient recipeIngredient = recipe.get(position);

        holder.recipeIngredient.setText(recipe.get(position).getName());
        holder.recipeIngredientQty.setText(qty);
        sufficientQtyBackground(holder, position);

        holder.recipeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.recipeIngredient.isChecked())
                {

                    tempList.add(recipeIngredient);
                }
                else
                {
                    tempList.remove(recipeIngredient);
                }
            }
        });
    }

    public ArrayList<Ingredient> selectedItems()
    {
        ArrayList<Ingredient> selectedItems = new ArrayList<>();
        selectedItems.addAll(tempList);
        tempList.clear();

        return selectedItems;
    }

    private void sufficientQtyBackground(MyViewHolder holder, int position) {
        if (!pantry.isEmpty()) {
            for (Ingredient pantryItem : pantry
            ) {
                Ingredient recipeIngredient = recipe.get(position);
                if(pantryContainsRecipeIngredient(recipeIngredient, pantryItem)) {
                    if (recipeIngredient.getQty() * modifier <= pantryItem.getQty()) {
                        holder.recipeIngredient.setBackgroundColor(Color.GREEN);
                    } else {
                        holder.recipeIngredient.setBackgroundColor(Color.RED);
                    }
                    break;
                }
                else
                {
                    holder.recipeIngredient.setBackgroundColor(Color.RED);
                }
            }
        }
        else
        {
            holder.recipeIngredient.setBackgroundColor(Color.RED);
        }
    }

    private boolean pantryContainsRecipeIngredient(Ingredient recipeIngredient, Ingredient pantryItem) {
        if( recipeIngredient.getName().equalsIgnoreCase(pantryItem.getName()))
        {
            return true;
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return recipe.size();
    }
}
