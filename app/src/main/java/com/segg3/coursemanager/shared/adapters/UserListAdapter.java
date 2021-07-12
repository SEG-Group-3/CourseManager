package com.segg3.coursemanager.shared.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.models.CardViewHolder;
import com.segg3.coursemanager.shared.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserListAdapter extends GenericCardAdapter<User> {


    public UserListAdapter(List<User> items) {
        super(items);
    }

    public UserListAdapter(List<User> value, View.OnClickListener onUserClicked) {
        super(value, onUserClicked);
    }

    @NonNull
    @NotNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course_layout, parent, false);
        v.setOnClickListener(onClickListener);

        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardViewHolder holder, int position) {
        User user = itemList.get(position);
        holder.title.setText(user.userName);
        holder.subtitle.setText(user.id);
        holder.subsubtitle.setText(user.type);
        try {
            if (user.type.equals("Student")) {
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_student));
            } else if (user.type.equals("Instructor")) {
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_instructor));
            } else {
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_admin));
            }
        } catch (Exception e) {
            Log.d("UserListAdapater", "User " + user.id + " is corrupted");
        }

    }
}
