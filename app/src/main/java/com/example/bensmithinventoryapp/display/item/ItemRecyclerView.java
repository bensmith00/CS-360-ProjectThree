package com.example.bensmithinventoryapp.display.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bensmithinventoryapp.ClickListener;
import com.example.bensmithinventoryapp.R;

import java.util.Collections;
import java.util.List;

/**
 * This class is a recycler view for item
 */
public class ItemRecyclerView extends RecyclerView.Adapter<ItemViewHolder> {

    List<Item> items = Collections.emptyList();
    Context context;
    ClickListener listener;

    /**
     * Constructor.
     * @param items - the items
     * @param context - the Context
     * @param listener - the Listener
     */
    public ItemRecyclerView(List<Item> items, Context context, ClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_card, parent, false);

        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        int index = holder.getAdapterPosition();
        holder.getItemName().setText(items.get(position).getItemName());
        holder.getItemQuantity().setText(String.valueOf(items.get(position).getItemQuantity()));
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listener.click(index);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
