package com.segg3.coursemanager.shared.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.R;

import org.jetbrains.annotations.NotNull;

public class CardViewHolder extends RecyclerView.ViewHolder {
    public final ImageView imageView;
    public final TextView title;
    public final TextView subtitle;
    public final TextView heading;

    public CardViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.card_title);
        subtitle = itemView.findViewById(R.id.card_subtitle);
        heading = itemView.findViewById(R.id.card_subsubtitle);
        imageView = itemView.findViewById(R.id.card_drawable_image);
    }
}