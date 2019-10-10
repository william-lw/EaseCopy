package com.william.androidsdk.widget.pageRecyclerView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class DisableScrollGridManager  extends GridLayoutManager {
    public DisableScrollGridManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DisableScrollGridManager(Context context) {
        super(context, 1);
    }

    public DisableScrollGridManager(Context context, int orientation, boolean reverseLayout) {
        super(context, 1, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
