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

import com.segg3.coursemanager.administrator.courses.ui.CourseViewFragment;
import com.segg3.coursemanager.administrator.users.ui.UsersViewFragment;
import com.segg3.coursemanager.auth.login.ui.LoginActivity;
import com.segg3.coursemanager.databinding.ActivityMainBinding;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.home.ui.HomeFragment;
import com.segg3.coursemanager.shared.models.CoursesViewModel;
import com.segg3.coursemanager.shared.models.UsersViewModel;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance;
    private ActivityMainBinding binding;
    private UsersViewModel users;
    private CoursesViewModel courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        users = new ViewModelProvider(this).get(UsersViewModel.class);
        courses = new ViewModelProvider(this).get(CoursesViewModel.class);

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
            } else if (item.getItemId() == R.id.nav_edit_courses) {
                UIUtils.swapViews(getSupportFragmentManager(), new CourseViewFragment());
            } else if (item.getItemId() == R.id.nav_edit_users) {
                UIUtils.swapViews(getSupportFragmentManager(), new UsersViewFragment());
            }

            closeDrawer();
            return true;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;

        binding.navigationMenu.getMenu().clear();
        binding.navigationMenu.inflateMenu(R.menu.base_menu);

        // If credentials were not stored go to login screen
        User u = users.getLoggedUser();
        if (u == null) {
            Log.v("ACCOUNT", "User is not logged in, redirecting to Login");
            ActivityOptions options = ActivityOptions.makeBasic();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent, options.toBundle());
            return;
        } else {
            View nav_header = binding.navigationMenu.getHeaderView(0);
            ((TextView) nav_header.findViewById(R.id.nav_head_username)).setText(u.getUsername());
            ((TextView) nav_header.findViewById(R.id.nav_head_usertype)).setText(u.getType());
            if (u.getType().toLowerCase().equals("admin"))
                binding.navigationMenu.inflateMenu(R.menu.admin_menu);
        }

        setContentView(binding.getRoot());
        UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());
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