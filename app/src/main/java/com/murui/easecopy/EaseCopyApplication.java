package com.murui.easecopy;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;

public class EaseCopyApplication extends Application {
    public static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        this.appContext = this;
    }
}
