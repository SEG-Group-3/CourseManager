package com.segg3.coursemanager.shared.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;


import com.segg3.coursemanager.R;
import com.segg3.coursemanager.User;
import com.segg3.coursemanager.shared.models.CardViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private final List<User> userList;
    private View.OnClickListener onClickListener;

    public UserListAdapter(List<User> userList2, View.OnClickListener listener) {
        onClickListener=listener;
        userList = userList2;
    }

    @NonNull
    @NotNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course_layout,parent,false);
        v.setOnClickListener(onClickListener);

        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardViewHolder holder, int position) {
        User user=userList.get(position);
        holder.title.setText(user.name);
        holder.subtitle.setText(user.getUserID());
        holder.subsubtitle.setText(user.getType());
        if (user.getType().equals("Student")){
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(),R.drawable.ic_student));
        }
        else if(user.getType().equals("Instructor")){
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(),R.drawable.ic_instructor));
        }
        else{
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(),R.drawable.ic_admin));
        }
        }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}