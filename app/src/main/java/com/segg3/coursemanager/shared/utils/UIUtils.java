package com.segg3.coursemanager.shared.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.R;

public class UIUtils {
    public static void swapViews(FragmentManager manager, Fragment next) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, next);
        transaction.commit();
    }

    public static void swipeFragmentLeft(FragmentManager manager, Fragment next) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.fragment_container, next);
        transaction.commit();
    }

    public static void swipeFragmentRight(FragmentManager manager, Fragment next) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragment_container, next);
        transaction.commit();
    }

    public static void setToolbarTitle(Activity activity, String title) {
        ActionBar bar = ((AppCompatActivity) activity).getSupportActionBar();
        if (bar != null)
            bar.setTitle(title);
    }

    public static void createYesNoMenu(String title, String message, Context ctx, DialogInterface.OnClickListener onYesListener) {
        new MaterialAlertDialogBuilder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", onYesListener)
                .setNegativeButton("No", (dialog, which) -> {
                })
                .show();
    }

    public static void createYesNoMenu(
            String title,
            String message,
            Context ctx,
            DialogInterface.OnClickListener onYesListener,
            DialogInterface.OnClickListener onNoListener) {

        new MaterialAlertDialogBuilder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", onYesListener)
                .setNegativeButton("No", onNoListener)
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

    public static TextWatcher createTextErrorRemover(TextInputLayout inputLayout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputLayout.setErrorEnabled(false);
            }
        };
    }

    public static void setRemoveErrorOnChange(TextInputLayout inputLayout) {
        inputLayout.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(inputLayout));
    }

    public static String getFieldText(TextInputLayout inputLayout) {
        return (inputLayout.getEditText()).getText().toString();
    }
}
