package com.segg3.coursemanager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.segg3.coursemanager.administrator.courses.ui.AdminCourseViewFragment;
import com.segg3.coursemanager.administrator.users.ui.AdminUsersViewFragment;
import com.segg3.coursemanager.databinding.ActivityMainBinding;
import com.segg3.coursemanager.instructor.courses.ui.InstructorCourseViewFragment;
import com.segg3.coursemanager.instructor.courses.ui.InstructorMyCourseViewFragment;
import com.segg3.coursemanager.shared.fragments.HomeFragment;
import com.segg3.coursemanager.shared.fragments.LoginActivity;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.viewmodels.AuthViewModel;
import com.segg3.coursemanager.shared.viewmodels.CoursesViewModel;
import com.segg3.coursemanager.shared.viewmodels.UsersViewModel;
import com.segg3.coursemanager.student.courses.ui.StudentCourseViewFragment;
import com.segg3.coursemanager.student.courses.ui.StudentMyCourseViewFragment;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance;
    public AuthViewModel auth;
    private ActivityMainBinding binding;
    private UsersViewModel users;
    private CoursesViewModel courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        users = new ViewModelProvider(this).get(UsersViewModel.class);
        courses = new ViewModelProvider(this).get(CoursesViewModel.class);
        auth = new ViewModelProvider(this).get(AuthViewModel.class);

        // Pre cache?
        users.getUsers();
        courses.getCourses();

        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle actionBarToggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(actionBarToggle);
        actionBarToggle.syncState();


        // Default Navigation
        binding.navigationMenu.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());
            } else if (item.getItemId() == R.id.nav_log_out) {
                // Delete cookies and go to login "cookies"
                ActivityOptions options = ActivityOptions.makeBasic();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent, options.toBundle());
                closeDrawer();
            }

            closeDrawer();
            return true;
        });

        auth.getUser().observe(this, this::createUserMenu);

        setContentView(binding.getRoot());


    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;

        // If credentials were not stored go to login screen
        User u = auth.getUser().getValue();
        createUserMenu(u);



        UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());
    }

    public void createUserMenu(User u) {

        binding.navigationMenu.getMenu().clear();
        binding.navigationMenu.inflateMenu(R.menu.base_menu);

        if (u == null) {
            Log.v("ACCOUNT", "User is not logged in, redirecting to Login");
            ActivityOptions options = ActivityOptions.makeBasic();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent, options.toBundle());
            return;
        } else {
            UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());

            View nav_header = binding.navigationMenu.getHeaderView(0);
            ((TextView) nav_header.findViewById(R.id.nav_head_username)).setText(u.userName);
            ((TextView) nav_header.findViewById(R.id.nav_head_usertype)).setText(u.type);
            if (u.type.toLowerCase().equals("admin")) {
                binding.navigationMenu.inflateMenu(R.menu.admin_menu);

                // Admin navigation
                binding.navigationMenu.setNavigationItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.nav_home) {
                        UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());
                    } else if (item.getItemId() == R.id.nav_log_out) {
                        // Delete cookies and go to login "cookies"
                        ActivityOptions options = ActivityOptions.makeBasic();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent, options.toBundle());
                        closeDrawer();
                    } else if (item.getItemId() == R.id.nav_edit_courses) {
                        UIUtils.swapViews(getSupportFragmentManager(), new AdminCourseViewFragment());
                    } else if (item.getItemId() == R.id.nav_edit_users) {
                        UIUtils.swapViews(getSupportFragmentManager(), new AdminUsersViewFragment());
                    }

                    closeDrawer();
                    return true;
                });
            } else if (u.type.toLowerCase().equals("instructor")) {
                binding.navigationMenu.inflateMenu(R.menu.instructor_menu);

                // Admin navigation
                binding.navigationMenu.setNavigationItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.nav_home) {
                        UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());
                    } else if (item.getItemId() == R.id.nav_log_out) {
                        // Delete cookies and go to login "cookies"
                        ActivityOptions options = ActivityOptions.makeBasic();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent, options.toBundle());
                        closeDrawer();
                    } else if (item.getItemId() == R.id.nav_instructor_edit_courses) {
                        UIUtils.swapViews(getSupportFragmentManager(), new InstructorCourseViewFragment());
                    } else if (item.getItemId() == R.id.nav_instructor_my_courses) {
                        UIUtils.swapViews(getSupportFragmentManager(), new InstructorMyCourseViewFragment());
                    }

                    closeDrawer();
                    return true;
                });


            } else {
                binding.navigationMenu.inflateMenu(R.menu.student_menu);

                // Admin navigation
                binding.navigationMenu.setNavigationItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.nav_home) {
                        UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());
                    } else if (item.getItemId() == R.id.nav_log_out) {
                        // Delete cookies and go to login "cookies"
                        ActivityOptions options = ActivityOptions.makeBasic();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent, options.toBundle());
                        closeDrawer();
                    } else if (item.getItemId() == R.id.nav_student_view_all_courses) {
                        UIUtils.swapViews(getSupportFragmentManager(), new StudentCourseViewFragment());
                    } else if (item.getItemId() == R.id.nav_student_my_courses) {
                        UIUtils.swapViews(getSupportFragmentManager(), new StudentMyCourseViewFragment());
                    }

                    closeDrawer();
                    return true;
                });

            }
        }
    }


    public void closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        closeDrawer();
        super.onBackPressed();
    }
}