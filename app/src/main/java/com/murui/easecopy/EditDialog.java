package com.murui.easecopy;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditDialog extends Dialog {
    public EditDialog(@NonNull Context context) {
        this(context,true, null);
    }


    protected EditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        View view = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null, false);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {

    }
}
