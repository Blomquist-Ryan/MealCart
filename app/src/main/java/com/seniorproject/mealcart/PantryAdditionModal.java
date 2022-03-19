package com.seniorproject.mealcart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PantryAdditionModal extends AppCompatDialogFragment {
    private PantryAdditionDialogListener listener;
    EditText qtyAddition;
    EditText nameAddition;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_ingredient_modal, null);
        builder.setView(view)
                .setTitle("Add Ingredient")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            if (!isIngredientAdditionEmpty())
                            {
                                String name = nameAddition.getText().toString();
                                float qty = Float.parseFloat(qtyAddition.getText().toString());
                                listener.applyAddition(name, qty);
                            }
                        }
                    });
        qtyAddition = view.findViewById(R.id.qtyAddition);
        nameAddition = view.findViewById(R.id.ingredientAddition);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PantryAdditionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement PantryAdditionDialogListner");
        }

    }

    public interface PantryAdditionDialogListener{
        void applyAddition(String name, float qty);

    }

    private boolean isIngredientAdditionEmpty()
    {
        String name = nameAddition.getText().toString();
        String qty = qtyAddition.getText().toString();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(qty))
        {
            return true;
        }
        return false;
    }
}
