package com.william.androidsdk.widget.pageRecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PageRecyclerView extends RecyclerView {

    private static final String TAG = PageRecyclerView.class.getSimpleName();
    private GPaginator paginator;
    private OnTouchActionUpListener touchListener;

    public enum TouchDirection {Horizontal, Vertical}

    private int currentFocusedPosition;
    private OnPagingListener onPagingListener;
    private int rows = 0;
    private int columns = 1;
    private float lastX, lastY;
    private OnChangeFocusListener onChangeFocusListener;
    private int originPaddingBottom;
    private int itemDecorationHeight = 0;

    public interface OnPagingListener {
        void onPageChange(int position, int itemCount, int pageSize, int currentPage);
    }

    public interface OnTouchActionUpListener {
        void onTouchActionUp(MotionEvent ev);
    }

    public interface OnChangeFocusListener {
        void onFocusChange(int prev, int current);
    }

    public PageRecyclerView(Context context) {
        super(context);
        init();
    }

    public PageRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCurrentFocusedPosition(int currentFocusedPosition) {
        int lastFocusedPosition = this.currentFocusedPosition;
        this.currentFocusedPosition = currentFocusedPosition;
        getAdapter().notifyItemChanged(lastFocusedPosition);
        getAdapter().notifyItemChanged(currentFocusedPosition);
        if (onChangeFocusListener != null) {
            onChangeFocusListener.onFocusChange(lastFocusedPosition, currentFocusedPosition);
        }
    }

    public int getItemDecorationHeight() {
        return itemDecorationHeight;
    }

    public void setItemDecorationHeight(int itemDecorationHeight) {
        this.itemDecorationHeight = itemDecorationHeight;
    }

    public int getCurrentFocusedPosition() {
        return currentFocusedPosition;
    }

    private void nextFocus(int focusedPosition) {
        if (!paginator.isItemInCurrentPage(focusedPosition)) {
            nextPage();
        }
        setCurrentFocusedPosition(focusedPosition);
    }

    public int getOriginPaddingBottom() {
        return originPaddingBottom;
    }

    private void prevFocus(int focusedPosition) {
        if (!paginator.isItemInCurrentPage(focusedPosition)) {
            prevPage();
        }
        setCurrentFocusedPosition(focusedPosition);
    }

    public void nextColumn() {
        int focusedPosition = paginator.nextColumn(currentFocusedPosition);
        if (focusedPosition < paginator.getSize()) {
            nextFocus(focusedPosition);
        }
    }

    public void prevColumn() {
        int focusedPosition = paginator.prevColumn(currentFocusedPosition);
        if (focusedPosition >= 0) {
            prevFocus(focusedPosition);
        }
    }

    public void nextRow() {
        int focusedPosition = paginator.nextRow(currentFocusedPosition);
        if (focusedPosition < paginator.getSize()) {
            nextFocus(focusedPosition);
        }
    }

    public void prevRow() {
        int focusedPosition = paginator.prevRow(currentFocusedPosition);
        if (focusedPosition >= 0) {
            prevFocus(focusedPosition);
        }
    }

    public void setOnChangeFocusListener(OnChangeFocusListener onChangeFocusListener) {
        this.onChangeFocusListener = onChangeFocusListener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (!(adapter instanceof PageAdapter)) {
            throw new IllegalArgumentException("Please use PageAdapter!");
        }
        rows = ((PageAdapter) adapter).getRowCount();
        columns = ((PageAdapter) adapter).getColumnCount();
        int size = ((PageAdapter) adapter).getDataCount();
        paginator = new GPaginator(rows, columns, size);
        paginator.setCurrentPage(0);

        LayoutManager layoutManager = getDisableLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanCount(columns);
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        originPaddingBottom = bottom;
    }

    public void setOffsetPaddingBottom(int offsetBottom) {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), offsetBottom);
    }

    public PageAdapter getPageAdapter() {
        PageAdapter pageAdapter = (PageAdapter) getAdapter();
        return pageAdapter;
    }

    public void resize(int newRows, int newColumns, int newSize) {
        if (paginator != null) {
            paginator.resize(newRows, newColumns, newSize);
        }
    }

    public void setCurrentPage(int currentPage) {
        if (paginator != null) {
            paginator.setCurrentPage(currentPage);
        }
    }

    public GPaginator getPaginator() {
        return paginator;
    }

    private void init() {
        originPaddingBottom = getPaddingBottom();
        setItemAnimator(null);
        setClipToPadding(true);
        setClipChildren(true);
        setLayoutManager(new DisableScrollLinearManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private TouchDirection touchDirection = TouchDirection.Vertical;

    private int detectDirection(MotionEvent currentEvent) {
        return PageTurningSimpleDetector.detectBothAxisTuring(getContext(), (int) (currentEvent.getX() - lastX), (int) (currentEvent.getY() - lastY));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int re = detectDirection(ev);
                return (re != PageTurningSimpleDirection.NONE);
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        touchListener.onTouchActionUp(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:

                int direction = detectDirection(ev);
                if (direction == PageTurningSimpleDirection.NEXT) {
                    nextPage();
                    super.onTouchEvent(ev);
                    return true;
                } else if (direction == PageTurningSimpleDirection.PREV) {
                    prevPage();
                    super.onTouchEvent(ev);
                    return true;
                } else if (direction == PageTurningSimpleDirection.NONE) {
                    return super.onTouchEvent(ev);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void setOnPagingListener(OnPagingListener listener) {
        this.onPagingListener = listener;
    }

    public void setOnTouchActionUpListener(OnTouchActionUpListener listener) {
        this.touchListener = listener;
    }



    public void prevPage() {
        if (paginator.prevPage()) {
            onPageChange();
        }
    }

    public void nextPage() {
        if (paginator.nextPage()) {
            onPageChange();
        }
    }

    public void gotoPage(int page) {
        if (paginator.gotoPage(page)) {
            onPageChange();
        }
    }

    private void onPageChange() {
        int position = paginator.getCurrentPageBegin();
        if (!paginator.isItemInCurrentPage(currentFocusedPosition)) {
            setCurrentFocusedPosition(position);
        }
        managerScrollToPosition(position);
        if (onPagingListener != null) {
            onPagingListener.onPageChange(position, getAdapter().getItemCount(), rows * columns, paginator.getCurrentPage());
        }
    }

    public void notifyDataSetChanged() {
        PageAdapter pageAdapter = getPageAdapter();
        int gotoPage = paginator.getCurrentPage() == -1 ? 0 : paginator.getCurrentPage();
        resize(pageAdapter.getRowCount(), pageAdapter.getColumnCount(), getPageAdapter().getDataCount());
        if (gotoPage > getPaginator().lastPage()) {
            gotoPage(getPaginator().lastPage());
        } else {
            pageAdapter.notifyDataSetChanged();
        }
    }

    private void managerScrollToPosition(int position) {
        getDisableLayoutManager().scrollToPositionWithOffset(position, 0);
    }

    private LinearLayoutManager getDisableLayoutManager() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager instanceof DisableScrollLinearManager) {
            layoutManager = (DisableScrollLinearManager) getLayoutManager();
        } else if (layoutManager instanceof DisableScrollGridManager) {
            layoutManager = (DisableScrollGridManager) getLayoutManager();
        }
        return layoutManager;
    }

    private boolean isClipView(Rect rect, View view) {
        switch (touchDirection) {
            case Horizontal:
                return (rect.right - rect.left) < view.getWidth();
            case Vertical:
                return (rect.bottom - rect.top) < view.getHeight();
        }
        return false;
    }

    public static abstract class PageAdapter<VH extends ViewHolder> extends Adapter<VH> {

        protected PageRecyclerView pageRecyclerView;

        public abstract int getRowCount();

        public abstract int getColumnCount();

        public abstract int getDataCount();

        public abstract VH onPageCreateViewHolder(ViewGroup parent, int viewType);

        public abstract void onPageBindViewHolder(VH holder, int position);

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            pageRecyclerView = (PageRecyclerView) parent;
            return onPageCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            final int adapterPosition = holder.getAdapterPosition();
            final View view = holder.itemView;
            if (view != null) {
                if (position < getDataCount()) {
                    view.setVisibility(VISIBLE);
                    onPageBindViewHolder(holder, adapterPosition);
                    updateFocusView(view, adapterPosition);
                } else {
                    view.setVisibility(INVISIBLE);
                }

                int paddingBottom = pageRecyclerView.getOriginPaddingBottom();
                int paddingTop = pageRecyclerView.getPaddingTop();
                int parentHeight = pageRecyclerView.getMeasuredHeight() - paddingBottom - paddingTop - getRowCount() * pageRecyclerView.getItemDecorationHeight();
                double itemHeight = ((double) parentHeight) / getRowCount();
                if (itemHeight > 0) {
                    int actualHeight = (int) Math.floor(itemHeight);
                    int offset = parentHeight - actualHeight * getRowCount();
                    view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actualHeight));
                    setOffsetPaddingBottom(offset);
                }
            }
        }

        private void setOffsetPaddingBottom(int offset) {
            int bottom = pageRecyclerView.getPaddingBottom();
            int offsetBottom = pageRecyclerView.getOriginPaddingBottom() + offset;
            if (offsetBottom != bottom) {
                pageRecyclerView.setOffsetPaddingBottom(offsetBottom);
            }
        }

        @Override
        public int getItemCount() {
            int itemCountOfPage = getRowCount() * getColumnCount();
            int size = getDataCount();
            if (size != 0) {
                int remainder = size % itemCountOfPage;
                if (remainder > 0) {
                    int blankCount = itemCountOfPage - remainder;
                    size = size + blankCount;
                }
            }
            if (pageRecyclerView != null) {
                pageRecyclerView.resize(getRowCount(), getColumnCount(), getDataCount());
            }
            return size;
        }

        private void updateFocusView(final View view, final int position) {
            if (position == pageRecyclerView.getCurrentFocusedPosition()) {
                view.setActivated(true);
            } else {
                view.setActivated(false);
            }

            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        pageRecyclerView.setCurrentFocusedPosition(position);
                    }
                    return false;
                }
            });
        }

        public PageRecyclerView getPageRecyclerView() {
            return pageRecyclerView;
        }
    }
}
