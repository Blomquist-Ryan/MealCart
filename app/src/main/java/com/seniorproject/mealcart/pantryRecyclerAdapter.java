package com.seniorproject.mealcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class pantryRecyclerAdapter extends RecyclerView.Adapter<pantryRecyclerAdapter.MyViewHolder>
{
    public interface OnItemClickListener{
        void increase(int position);
        void decrease(int position);
    }
    public void setOnItemClickListener(pantryRecyclerAdapter.OnItemClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    private ArrayList<Ingredient> ingredientList;

    private pantryRecyclerAdapter.OnItemClickListener clickListener;

        public pantryRecyclerAdapter(ArrayList<Ingredient> ingredientList) {
            this.ingredientList = ingredientList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            private TextView name;
            private TextView qty;
            private Button increase;
            private Button decrease;


            public MyViewHolder(final View view, OnItemClickListener listener) {
                super(view);
                name = view.findViewById(R.id.name);
                qty = view.findViewById(R.id.qty);
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
    public pantryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View ingredientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new MyViewHolder(ingredientView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull pantryRecyclerAdapter.MyViewHolder holder, int position)
    {
        String name = ingredientList.get(position).getName();
        float qty = (float)ingredientList.get(position).getQty();

        holder.name.setText(name);
        holder.qty.setText(String.valueOf(qty));
    }

    @Override
    public int getItemCount()
    {
        return ingredientList.size();
    }

    public ArrayList<Ingredient> updateItems()
    {
        return ingredientList;
    }
}
