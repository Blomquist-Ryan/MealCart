package com.seniorproject.mealcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealPlanRecyclerAdapter extends RecyclerView.Adapter<MealPlanRecyclerAdapter.MyViewHolder>
{
    public interface OnItemClickListener{
        void make(int position);
        void plan(int position);
    }
    public void setOnItemClickListener(MealPlanRecyclerAdapter.OnItemClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    private ArrayList<Ingredient> ingredientList;

    private MealPlanRecyclerAdapter.OnItemClickListener clickListener;

    public MealPlanRecyclerAdapter() {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView date;
        private TextView meal;
        private Button planMealButton;
        private Button makeMealButton;


        public MyViewHolder(final View view, MealPlanRecyclerAdapter.OnItemClickListener listener) {
            super(view);
            date = view.findViewById(R.id.date);
            meal = view.findViewById(R.id.dinner);
            planMealButton = view.findViewById(R.id.plan);
            makeMealButton = view.findViewById(R.id.make);
            planMealButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION)
                        {
                            listener.plan(position);
                        }
                    }
                }
            });

            makeMealButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION)
                        {
                            listener.make(position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public MealPlanRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View ingredientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_plan, parent, false);
        return new MealPlanRecyclerAdapter.MyViewHolder(ingredientView, clickListener);
    }

    @Override
    public int getItemCount()
    {
        return ingredientList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanRecyclerAdapter.MyViewHolder holder, int position)
    {
        String name = ingredientList.get(position).getName();
        float qty = ingredientList.get(position).getQty();

        holder.date.setText(name);
        holder.meal.setText(String.valueOf(qty));
    }

    public ArrayList<Ingredient> updateItems()
    {
        return ingredientList;
    }
}
