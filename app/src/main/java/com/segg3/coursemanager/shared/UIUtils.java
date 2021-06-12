package com.segg3.coursemanager.shared;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.User;

import org.jetbrains.annotations.NotNull;

public class UIUtils {
    public static void swapViews(FragmentManager manager, Fragment next){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, next);
        transaction.commit();
    }

    public static void swipeFragmentLeft(FragmentManager manager, Fragment next){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.fragment_container, next);
        transaction.commit();
    }

    public static void swipeFragmentRight(FragmentManager manager, Fragment next){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_container, next);
        transaction.commit();
    }

    public static void setToolbarTitle(Activity activity, String title){
        ActionBar bar  = ((AppCompatActivity)activity).getSupportActionBar();
        if (bar != null)
            bar.setTitle(title);
    }

    public static void createYesNoMenu(String title, String message, Context ctx, DialogInterface.OnClickListener onYesListener){
        new MaterialAlertDialogBuilder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, onYesListener)
                .setNegativeButton(android.R.string.no, (dialog, which) -> {})
                .show();
    }

    public static void createToast(Context applicationContext, CharSequence msg) {
        Toast.makeText(applicationContext,
                msg,
                Toast.LENGTH_LONG).show();
    }


    public static void createToast(Context applicationContext, int string_id) {
        Toast.makeText(applicationContext,
                string_id,
                Toast.LENGTH_LONG).show();
    }
}
