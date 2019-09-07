package com.murui.easecopy;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.murui.easecopy.bean.ClipboardBean;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private TextView textView;
    private RecyclerView contentRecyclerView;
    private ClipContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenParam.getInstance().init(this);
        initViews();
    }

    private void initViews() {
        contentRecyclerView = findViewById(R.id.show_clipboard);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void showEditDialog() {

    }

    private void hideFloatButton() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, FloatViewService.class));
        stopService(intent);
    }

    private void showFloatButton() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, FloatViewService.class));
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ClipContentAdapter();
        List<ClipboardBean> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ClipboardBean clipboardBean = new ClipboardBean();
            clipboardBean.setClipContent("dddddd "+i);
            list.add(clipboardBean);
        }
        adapter.setDateList(list);
        contentRecyclerView.setAdapter(adapter);
    }
}
