package com.example.bensmithinventoryapp.display.item;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bensmithinventoryapp.R;

/**
 * This class holds the item view
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView itemName;
    private TextView itemQuantity;
    private View view;

    /**
     * Constructor.
     * @param itemView - the item view
     */
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.itemName);
        itemQuantity = itemView.findViewById(R.id.itemQuantity);
        view = itemView;
    }

    /**
     * Gets the item name.
     * @return - the item name
     */
    public TextView getItemName() {
        return itemName;
    }

    /**
     * Gets the item quantity.
     * @return - the item quantity
     */
    public TextView getItemQuantity() {
        return itemQuantity;
    }

    /**
     * Gets the view.
     * @return - the view
     */
    public View getView() {
        return view;
    }
}
