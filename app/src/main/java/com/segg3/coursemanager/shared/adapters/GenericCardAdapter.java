package com.segg3.coursemanager.shared.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.models.CardViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericCardAdapter<T> extends RecyclerView.Adapter<CardViewHolder> {
    public final List<T> itemList;
    public View.OnClickListener onClickListener;
    @NonNull
    public List<CardViewHolder> viewHolders = new ArrayList<>();

    public GenericCardAdapter(List<T> items) {
        itemList = items;
    }

    public GenericCardAdapter(List<T> items, View.OnClickListener listener) {
        itemList = items;
        onClickListener = listener;
    }


    @NonNull
    @NotNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course_layout, parent, false);
        v.setOnClickListener(onClickListener);
        CardViewHolder viewHolder = new CardViewHolder(v);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        onClickListener = listener;

        for (CardViewHolder view : viewHolders) {
            view.itemView.setOnClickListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
