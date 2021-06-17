package com.segg3.coursemanager.shared.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.models.CardViewHolder;
import com.segg3.coursemanager.shared.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private final View.OnClickListener onClickListener;
    private List<User> userList;

    public UserListAdapter(List<User> users, View.OnClickListener listener) {
        onClickListener = listener;
        userList = users;
    }

    public void submitList(List<User> users) {
        userList = users;
        notifyDataSetChanged();
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
        User user = userList.get(position);
        holder.title.setText(user.userName);
        holder.subtitle.setText(user.type);
        holder.heading.setText(user.id);
        switch (user.type.toLowerCase()) {
            case "student":
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_student));
                break;
            case "instructor":
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_instructor));
                break;
            case "admin":
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_admin));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
