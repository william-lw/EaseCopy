package com.murui.easecopy;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class EaseCopyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
