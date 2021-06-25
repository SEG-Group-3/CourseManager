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

import com.segg3.coursemanager.administrator.courses.ui.CourseViewFragment;
import com.segg3.coursemanager.administrator.users.ui.UsersViewFragment;
import com.segg3.coursemanager.auth.login.ui.LoginActivity;
import com.segg3.coursemanager.databinding.ActivityMainBinding;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.fragments.HomeFragment;
import com.segg3.coursemanager.shared.models.User;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());


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
            if (item.getItemId() == R.id.nav_home){
                UIUtils.swapViews(getSupportFragmentManager(),new HomeFragment());
            } else if (item.getItemId() == R.id.nav_log_out) {
                // Delete cookies and go to login "cookies"
                ActivityOptions options = ActivityOptions.makeBasic();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent, options.toBundle());
                closeDrawer();
            } else if (item.getItemId()==R.id.nav_edit_courses){
                UIUtils.swapViews(getSupportFragmentManager(),new CourseViewFragment());
            } else if (item.getItemId()==R.id.nav_edit_users){
                UIUtils.swapViews(getSupportFragmentManager(),new UsersViewFragment());
            }
            
            closeDrawer();
            return true;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // If credentials were not stored go to login screen
        User u = ((AccountAccess) AccountAccess.getInstance()).getUser();

        if (u == null) {
            Log.v("ACCOUNT", "User is not logged in, redirecting to Login");
            ActivityOptions options = ActivityOptions.makeBasic();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent, options.toBundle());
            return;
        }

        View nav_header =  binding.navigationMenu.getHeaderView(0);
        ((TextView)nav_header.findViewById(R.id.nav_head_username)).setText(u.userName);
        ((TextView)nav_header.findViewById(R.id.nav_head_usertype)).setText(u.type);

        binding.navigationMenu.getMenu().clear();
        binding.navigationMenu.inflateMenu(R.menu.base_menu);
        if(u.type.toLowerCase().equals("admin"))
            binding.navigationMenu.inflateMenu(R.menu.admin_menu);
        //TODO inflate menus for other user types



        setContentView(binding.getRoot());
        UIUtils.swapViews(getSupportFragmentManager(), new HomeFragment());



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