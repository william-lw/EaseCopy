package com.murui.easecopy;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;

import androidx.databinding.BindingAdapter;



public class DataBindingUtils {

    @BindingAdapter("setBackground")
    public static void setBackground(View view, boolean itemCheckState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Resources resources = view.getResources();
            if (itemCheckState) {
                view.setBackground(resources.getDrawable(R.drawable.item_checked));
            } else {
                view.setBackground(resources.getDrawable(R.drawable.item_unchecked));
            }
        }
    }
}
