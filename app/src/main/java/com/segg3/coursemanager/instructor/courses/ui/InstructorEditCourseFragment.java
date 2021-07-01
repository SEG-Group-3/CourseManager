package com.segg3.coursemanager.instructor.courses.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.NumberPicker;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.DialogCourseHourBinding;
import com.segg3.coursemanager.databinding.FragmentInstructorEditCourseBinding;
import com.segg3.coursemanager.shared.adapters.CourseHoursListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.CourseHours;
import com.segg3.coursemanager.shared.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstructorEditCourseFragment extends Fragment {
    FragmentInstructorEditCourseBinding binding;
    Course beingEdited;

    CourseHoursListAdapter courseListAdapter;
    RecyclerView recyclerView;
    List<CourseHours> mutCourseHours;
    DialogCourseHourBinding courseHourBinding;

    int position = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstructorEditCourseBinding.inflate(inflater);
        View view = binding.getRoot();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
            }
        });

        TextInputLayout[] emptyCheckedFields = {binding.courseDescriptionInput, binding.courseCapacityInput};

        for (TextInputLayout field :
                emptyCheckedFields) {
            UIUtils.setRemoveErrorOnChange(field);
        }


        binding.cancelButton.setOnClickListener(this::onCancelEdit);
        binding.applyButton.setOnClickListener(this::onApplyEdit);

        binding.courseDescriptionInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.courseDescriptionInput));
        binding.courseCapacityInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.courseCapacityInput));

        setHasOptionsMenu(true);
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.edit_course));
        String courseCode = getArguments().getString("code");
        beingEdited = CoursesDao.getInstance().getCourse(courseCode);
        binding.courseCapacityInput.getEditText().setText(String.valueOf(beingEdited.capacity));
        binding.courseDescriptionInput.getEditText().setText(beingEdited.description);



        // Add an hour when button is clicked
        binding.courseHoursFab.setOnClickListener(this::onAddCourseClicked);
        // TODO Populate course hours recycler view
        recyclerView = binding.courseHoursRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        mutCourseHours = new ArrayList<>();
        for (String c : beingEdited.courseHours) {
            mutCourseHours.add(new CourseHours(c));
        }

        courseListAdapter = new CourseHoursListAdapter(mutCourseHours, this::onCourseHourClicked);
        recyclerView.setAdapter(courseListAdapter);


        // Swipe stuff to the left to delete it
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mutCourseHours.remove(position);
                updateRecyclerView();
                UIUtils.createToast(getContext(), "Swiped something...");
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        // binding.courseHoursRecyclerView...

        return view;
    }

    private void onAddCourseClicked(View view) {
        MaterialAlertDialogBuilder builder = CreateCourseHoursBuilder()
                .setPositiveButton("Add", (dialog, which) -> {
                    CourseHours cw = new CourseHours(
                            courseHourBinding.weekdayPicker.getValue() + 1,
                            courseHourBinding.hourPicker.getValue(),
                            10 * courseHourBinding.minutePicker.getValue(),
                            30 + 5 * courseHourBinding.durationPicker.getValue());


                    // TODO check if cw intersect with preexisting course hours
                    // if it intersects show error
                    // if its ok, replace previous value
                    mutCourseHours.add(cw);
                    updateRecyclerView();
                } )
                .setNegativeButton("Cancel", (dialog, which) -> {});
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        UIUtils.createYesNoMenu("Unassign from course", "Do you really want to unassign this course?", getActivity(), (dialog, which) -> {
            CoursesDao.getInstance().unAssignInstructor(beingEdited.code);
            UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private MaterialAlertDialogBuilder CreateCourseHoursBuilder(){
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.dialog_course_hour, recyclerView, false);
        courseHourBinding = DialogCourseHourBinding.bind(popupView);
        // Set Weekdays
        NumberPicker weekPicker = popupView.findViewById(R.id.weekday_picker);
        List<String> values = new ArrayList<>();
        for (DayOfWeek dayOfWeek: DayOfWeek.values()){
            values.add(dayOfWeek.toString());
        }
        String[] valuesString = new String[DayOfWeek.values().length];
        values.toArray(valuesString);
        weekPicker.setDisplayedValues(valuesString);
        weekPicker.setMaxValue(valuesString.length-1);
        weekPicker.setMinValue(0);

        // Set Hour
        NumberPicker hourPicker = popupView.findViewById(R.id.hour_picker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);

        // Set Minutes
        NumberPicker minutePicker = popupView.findViewById(R.id.minute_picker);

        // Only show numbers in steps of 10
        int minDuration = 0;
        int maxDuration = 60;
        String[] minutesStrings = new String[(maxDuration-minDuration)/10];
        for (int i = 0; i < minutesStrings.length; i++) {
            minutesStrings[i] = String.valueOf(minDuration + i * 10);
        }

        minutePicker.setDisplayedValues(minutesStrings);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(minutesStrings.length-1);


        // Set Duration
        NumberPicker durationPicker = popupView.findViewById(R.id.duration_picker);

        // Only show numbers in steps of 5
        minDuration = 30;
        maxDuration = 180;
        String[] durationStrings = new String[(maxDuration-minDuration)/5];
        for (int i = 0; i < durationStrings.length; i++) {
            durationStrings[i] = String.valueOf(minDuration + i * 5);
        }

        durationPicker.setDisplayedValues(durationStrings);
        durationPicker.setMinValue(0);
        durationPicker.setMaxValue(durationStrings.length-1);
        MaterialAlertDialogBuilder builder = new  MaterialAlertDialogBuilder(getContext())
                .setTitle("Pick a time")
                .setView(popupView);

        return builder;
    }

    private void updateRecyclerView(){
        courseListAdapter = new CourseHoursListAdapter(mutCourseHours, this::onCourseHourClicked);
        recyclerView.setAdapter(courseListAdapter);
    }

    private void onCourseHourClicked(View v) {
        MaterialAlertDialogBuilder builder = CreateCourseHoursBuilder()
                .setPositiveButton("Apply", (dialog, which) -> {
                    CourseHours cw = new CourseHours(
                            courseHourBinding.weekdayPicker.getValue() + 1,
                            courseHourBinding.hourPicker.getValue(),
                            10 * courseHourBinding.minutePicker.getValue(),
                            30 + 5 * courseHourBinding.durationPicker.getValue());


                    // TODO check if cw intersect with preexisting course hours
                    // if it intersects show error
                    // if its ok, replace previous value
                    int position = recyclerView.getChildLayoutPosition(v);
                    mutCourseHours.remove(position);
                    mutCourseHours.add(cw);
                    updateRecyclerView();
                } )
                .setNegativeButton("Cancel", (dialog, which) -> {});
        builder.show();
    }

    private void onCancelEdit(View v) {
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
    }

    private void onApplyEdit(View v) {
        // If invalid username or password, shake!
        boolean ok = true;
        TextInputLayout[] emptyCheckedFields = {binding.courseDescriptionInput, binding.courseCapacityInput};

        for (TextInputLayout field :
                emptyCheckedFields) {
            if (UIUtils.getFieldText(field).isEmpty()) {
                field.setError(getString(R.string.error_empty_field));
                field.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
                ok = false;
            }
        }

        String capacity = binding.courseCapacityInput.getEditText().getText().toString();
        try {
            if (Integer.parseUnsignedInt(capacity) <= 0) {
                binding.courseCapacityInput.setError("Course capacity should be greater than one!");
                binding.courseCapacityInput.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
            }
        } catch (NumberFormatException exception) {
            ok = false;
            binding.courseCapacityInput.setError("Not a number! exception");
            binding.courseCapacityInput.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
        }


        if (!ok)
            return;


        // TODO Update course date
        beingEdited.description = binding.courseDescriptionInput.getEditText().getText().toString();
        beingEdited.capacity = Integer.parseUnsignedInt(capacity);


        List<String> courseHoursStr = new ArrayList<>();
        for (CourseHours c:mutCourseHours) {
            courseHoursStr.add(c.toString());
        }
        beingEdited.courseHours = courseHoursStr;
        CoursesDao.getInstance().editCourse(beingEdited.code, beingEdited);
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
    }
}