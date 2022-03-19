package com.seniorproject.mealcart;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Struct;
import java.util.ArrayList;

import kotlin.jvm.internal.markers.KMappedMarker;

import static android.content.Context.MODE_PRIVATE;

public class ShoppingCartRecyclerAdapter extends RecyclerView.Adapter<ShoppingCartRecyclerAdapter.MyViewHolder>{

    public interface OnItemClickListener{
        void increase(int position);
        void decrease(int position);
    }
    public void setOnItemClickListener(ShoppingCartRecyclerAdapter.OnItemClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    private ShoppingCartRecyclerAdapter.OnItemClickListener clickListener;


    private ArrayList<Ingredient> shoppingCart;
    private ArrayList<Ingredient> temporaryList = new ArrayList<>();
    public SparseBooleanArray checkboxCheckStates = new SparseBooleanArray();


    public ShoppingCartRecyclerAdapter(ArrayList<Ingredient> shoppingCart)
    {
        this.shoppingCart = shoppingCart;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView qty;
        private CheckBox item;
        private Button increase;
        private Button decrease;
        public MyViewHolder(final View view, ShoppingCartRecyclerAdapter.OnItemClickListener listener)
        {
            super(view);
            item = view.findViewById(R.id.shoppingCartItem);
            qty = view.findViewById(R.id.shoppingCartQty);

            increase = view.findViewById(R.id.increaseQuantity);
            decrease = view.findViewById(R.id.decreaseQuantity);
            decrease.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION)
                        {
                            listener.decrease(position);
                        }
                    }
                }
            });

            increase.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION)
                        {
                            listener.increase(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ShoppingCartRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View shoppingCartView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart_item, parent, false);
        return new MyViewHolder(shoppingCartView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartRecyclerAdapter.MyViewHolder holder, int position) {

        if (!checkboxCheckStates.get(position, false))
        {
            holder.item.setChecked(false);
        }
        else
        {
            holder.item.setChecked(true);
        }
        buildViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }
    public ArrayList<Ingredient> getItemAdditions()
    {
        return temporaryList;
    }

    private void buildRemovalList(@NonNull ShoppingCartRecyclerAdapter.MyViewHolder holder, int position)
    {
        String name = shoppingCart.get(position).getName();
        float qty = shoppingCart.get(position).getQty();

        if(holder.item.isChecked())
        {
            checkboxCheckStates.put(position, true);
            Ingredient ingredient = new Ingredient(name, qty);
            temporaryList.add(ingredient);
        }
        else {
            checkboxCheckStates.put(position, false);
            Ingredient cartIngredient = shoppingCart.get(position);
            Ingredient removeIngredient = itemInTemporaryCart(cartIngredient);
            removeIngredientFromTemporaryList(removeIngredient, cartIngredient);
        }
    }

    private void removeIngredientFromTemporaryList(Ingredient removeIngredient, Ingredient cartIngredient)
    {
        if(!removeIngredient.equals(cartIngredient))
        {
            temporaryList.remove(removeIngredient);
        }
    }

    private Ingredient itemInTemporaryCart(Ingredient ingredient)
    {
        Ingredient tempIngredient = ingredient;
        for (Ingredient i:temporaryList
        ) {
            if(ingredient.getName().equals(i.getName()))
            {
                tempIngredient = i;
                break;
            }
        }
        return tempIngredient;
    }


    private void buildViewHolder(@NonNull ShoppingCartRecyclerAdapter.MyViewHolder holder, int position)
    {
        String name = shoppingCart.get(position).getName();
        float qty = shoppingCart.get(position).getQty();

        holder.item.setText(name);
        holder.qty.setText(String.valueOf(qty));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildRemovalList(holder, position);
            }
        });
    }

    public void update(ArrayList<Ingredient> shoppingCart)
    {
        this.shoppingCart = shoppingCart;
    }
    public void clearTemporaryList()
    {
        checkboxCheckStates.clear();
        temporaryList.clear();
    }




}
