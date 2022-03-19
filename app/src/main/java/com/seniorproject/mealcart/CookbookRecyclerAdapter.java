package com.seniorproject.mealcart;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CookbookRecyclerAdapter extends RecyclerView.Adapter<CookbookRecyclerAdapter.MyViewHolder>
{
    public interface OnItemClickListener{

        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener clickListener)
{
    this.clickListener = clickListener;
}
    private ArrayList<Recipe> cookbook;
    private OnItemClickListener clickListener;
    public CookbookRecyclerAdapter(ArrayList<Recipe> cookbook)
    {
        this.cookbook = cookbook;
    }

    public void OnItemClickListener(OnItemClickListener listener)
    {
        clickListener = listener;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView cookbookItemName;
        public MyViewHolder(final View view, OnItemClickListener listener) {
            super(view);
            cookbookItemName = view.findViewById(R.id.cookbookItemName);
            cookbookItemName.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if( position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public CookbookRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cookbook_item, parent, false);
        return new CookbookRecyclerAdapter.MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CookbookRecyclerAdapter.MyViewHolder holder, int position)
    {
        String name = cookbook.get(position).getName();
        holder.cookbookItemName.setText(name);

    }

    @Override
    public int getItemCount()
    {
        return  cookbook.size();
    }

    public void filter(ArrayList<Recipe> filteredRecipes) {
        cookbook = filteredRecipes;
        notifyDataSetChanged();
    }
}
