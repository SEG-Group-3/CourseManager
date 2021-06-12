package com.segg3.coursemanager.shared.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.Course;
import com.segg3.coursemanager.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {
    private final List<Course> courseList;
    private View.OnClickListener onClickListener;

    public CourseListAdapter(List<Course> courseList2, View.OnClickListener listener) {
        onClickListener=listener;
        courseList = courseList2;
    }

    @NonNull
    @NotNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course_layout,parent,false);
        v.setOnClickListener(onClickListener);
        return new CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseViewHolder holder, int position) {
        holder.courseName.setText(courseList.get(position).name);
        holder.courseCode.setText(courseList.get(position).code);

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseCode;
        private final TextView courseName;
        public CourseViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            courseCode=itemView.findViewById(R.id.course_code_text);
            courseName=itemView.findViewById(R.id.couse_name_text);
        }


    }

}
