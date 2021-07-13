package com.segg3.coursemanager.shared.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.models.CardViewHolder;
import com.segg3.coursemanager.shared.models.CourseHours;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseHoursListAdapter extends GenericCardAdapter<CourseHours> {


    public CourseHoursListAdapter(List<CourseHours> items) {
        super(items);
    }

    public CourseHoursListAdapter(List<CourseHours> mutCourseHours, View.OnClickListener onCourseHourClicked) {
        super(mutCourseHours, onCourseHourClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardViewHolder holder, int position) {
        holder.title.setText(itemList.get(position).weekDay.toString());
        holder.subtitle.setText(itemList.get(position).start.toString());
        holder.subsubtitle.setText(itemList.get(position).durations + " minutes");
        holder.imageView.setImageDrawable(AppCompatResources.getDrawable(holder.imageView.getContext(), R.drawable.ic_clock));

    }

}
