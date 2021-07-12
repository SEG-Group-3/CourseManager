package com.segg3.coursemanager.shared.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.models.CardViewHolder;
import com.segg3.coursemanager.shared.models.CourseHours;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseHoursListAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private final List<CourseHours> courseHoursList;
    private View.OnClickListener onClickListener;

    public CourseHoursListAdapter(List<CourseHours> courseHours, View.OnClickListener listener) {
        onClickListener = listener;
        courseHoursList = courseHours;
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
        holder.title.setText(courseHoursList.get(position).weekDay.toString());
        holder.subtitle.setText(courseHoursList.get(position).start.toString());
        holder.subsubtitle.setText(courseHoursList.get(position).durations + " minutes");
        holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_clock));

    }

    @Override
    public int getItemCount() {
        return courseHoursList.size();
    }


}
