package com.segg3.coursemanager.shared.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.Course;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.CardViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private final List<Course> courseList;
    private View.OnClickListener onClickListener;

    public CourseListAdapter(List<Course> courses, View.OnClickListener listener) {
        onClickListener=listener;
        courseList = courses;
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
        holder.title.setText(courseList.get(position).name);
        holder.subtitle.setText(courseList.get(position).code);
        holder.subsubtitle.setText(courseList.get(position).getId());
        holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(),R.drawable.ic_book));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


}
