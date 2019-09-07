package com.murui.easecopy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.murui.easecopy.ClipAdapter;
import com.murui.easecopy.OnFloatWindowClickListener;
import com.murui.easecopy.R;
import com.murui.easecopy.bean.ClipboardBean;

import java.util.List;

public class ClipBoardContentView extends LinearLayout {

    private Context mContext;
    private RecyclerView clipContent;
    private TextView clipTitle;
    private ClipAdapter clipAdapter;
    private OnFloatWindowClickListener listener;

    public ClipBoardContentView(Context context) {
        this(context, null);
    }

    public ClipBoardContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipBoardContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.clip_content_view, this, true);
        initViews(view);
    }

    private void initViews(View view) {
        clipTitle = view.findViewById(R.id.clip_title);
        clipContent = view.findViewById(R.id.clip_content);
        clipContent.setLayoutManager(new LinearLayoutManager(mContext));
        clipAdapter = new ClipAdapter();
        clipContent.setAdapter(clipAdapter);
        view.findViewById(R.id.out_side).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOutsideClick();
            }
        });
    }

    public void setDataList(List<ClipboardBean> list) {
        if (clipAdapter != null) {
            clipAdapter.setDateList(list);
        }
    }


    public void setOnClipTextItemClickListener(OnFloatWindowClickListener listener) {
        if (clipAdapter != null) {
            this.listener = listener;
            clipAdapter.setOnClipTextItemClickListener(listener);
        }
    }

}
