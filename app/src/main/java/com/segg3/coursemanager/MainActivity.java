package com.segg3.coursemanager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.segg3.coursemanager.administrator.courses.ui.CourseViewFragment;
import com.segg3.coursemanager.auth.login.ui.LoginActivity;
import com.segg3.coursemanager.databinding.ActivityMainBinding;
import com.segg3.coursemanager.shared.home.ui.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle actionBarToggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close );
        binding.drawerLayout.addDrawerListener(actionBarToggle);
        actionBarToggle.syncState();

        // Default Navigation
        binding.navigationMenu.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_log_in) {
                ActivityOptions options = ActivityOptions.makeBasic();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent, options.toBundle());
                closeDrawer();
            } else if (item.getItemId() == R.id.nav_home){
                swapViews(new HomeFragment());
            }
            else if (item.getItemId()==R.id.nav_edit_course){
                swapViews(new CourseViewFragment());
            }
            closeDrawer();
            return true;
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        // If credentials were not stored go to login screen
//         ActivityOptions options = ActivityOptions.makeBasic();
//         Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//         startActivity(intent, options.toBundle());

        // else start normally
        setContentView(binding.getRoot());
        swapViews(new HomeFragment());
    }

    public void swapViews(Fragment next){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, next).commit();
    }

    public void closeDrawer(){
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        closeDrawer();
        super.onBackPressed();
    }
}