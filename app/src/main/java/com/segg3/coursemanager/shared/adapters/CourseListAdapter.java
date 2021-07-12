package com.segg3.coursemanager.shared.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.models.CardViewHolder;
import com.segg3.coursemanager.shared.models.Course;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseListAdapter extends GenericCardAdapter<Course> {
    public CourseListAdapter(List<Course> items, View.OnClickListener listener) {
        super(items, listener);
    }

    public CourseListAdapter(List<Course> coursesL) {
        super(coursesL);
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
        holder.title.setText(itemList.get(position).name);
        holder.subtitle.setText(itemList.get(position).code);
        holder.subsubtitle.setText(itemList.get(position).instructor);
        holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_book));
    }
}
