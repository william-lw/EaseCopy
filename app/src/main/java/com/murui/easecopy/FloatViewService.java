package com.murui.easecopy;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.murui.easecopy.bean.ClipboardBean;
import com.murui.easecopy.bean.ClipboardDBModel;
import com.murui.easecopy.util.ClipboardUtil;
import com.murui.easecopy.view.ClipBoardContentView;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FloatViewService extends Service {

    private static final String TAG = "FloatViewService";
    // 定义浮动窗口布局
    private LinearLayout mFloatLayout;
    private LinearLayout mFloatWindow;
    private WindowManager.LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private ImageButton mFloatView;
    private int screenHeight;
    private int screenWidth;
    private MyCountDownTimer myCountDownTimer;
    private LayoutParams popupParams;
    private ClipBoardContentView popupContent;
    private volatile boolean showPopup = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        screenHeight = ScreenParam.getInstance().height;
        screenWidth = ScreenParam.getInstance().width;
        createFloatView();
        myCountDownTimer = new MyCountDownTimer(2500, 1000);
        myCountDownTimer.start();
    }

//    public static void

    @SuppressWarnings("static-access")
    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    private void createFloatView() {
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        initFloatButtonView(inflater);
        initClipboardContentView(inflater);
        setOnFloatViewListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnFloatViewListener() {
        // 设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() {

            private float rawX;
            private float rawY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "ACTION_DOWN");
                        mFloatLayout.setAlpha(1.0f);
                        myCountDownTimer.cancel();

                        rawX = event.getRawX();
                        rawY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "ACTION_MOVE");
                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        int distanceX = (int) (event.getRawX() - rawX);
                        int distanceY = (int) (event.getRawY() - rawY);
                        wmParams.x = wmParams.x + distanceX;
                        wmParams.y = wmParams.y + distanceY;
                        // 刷新
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        rawX = event.getRawX();
                        rawY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "ACTION_UP");
                        myCountDownTimer.start();
                        if (wmParams.x > screenWidth/2) {
                            wmParams.x = screenWidth - mFloatView.getMeasuredWidth() / 2;
                        } else {
                            wmParams.x = 0;
                        }
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        break;
                }
                return false;
            }
        });

        mFloatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showPopup) {
                    showPopupWindow();
                    setFlotWindowContent();
                } else {
                    hidePopupWindow();
                }
                showPopup = !showPopup;
            }
        });

        popupContent.setOnClipTextItemClickListener(new OnFloatWindowClickListener() {
            @Override
            public void onClipItemClick(ClipboardBean text) {
                ClipboardUtil.copyTextToClipboard(getApplicationContext(), text.getClipContent());
                Toast.makeText(getApplicationContext(), R.string.copy_success, Toast.LENGTH_SHORT).show();
                hidePopupWindow();
            }

            @Override
            public void onOutsideClick() {
                hidePopupWindow();
            }
        });

    }

    private void hidePopupWindow() {
        if (mWindowManager != null && mFloatWindow != null && showPopup) {
            mWindowManager.removeView(mFloatWindow);
            showPopup = false;
        }
    }

    private void showPopupWindow() {
        if (mWindowManager != null && mFloatWindow != null && popupParams != null) {
            mWindowManager.addView(mFloatWindow, popupParams);
        }
    }

    private void initFloatButtonView(LayoutInflater inflater) {
        wmParams = new LayoutParams();
        // 通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(
                getApplication().WINDOW_SERVICE);
        // 设置window type

        setVmType(wmParams);

        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 150;
        // 设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;
        // 获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(
                R.layout.alert_window_menu, null);
        // 添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

        // 浮动窗口按钮
        mFloatView = (ImageButton) mFloatLayout
                .findViewById(R.id.alert_window_imagebtn);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    private void setVmType(WindowManager.LayoutParams wmParams) {
        if (Build.VERSION.SDK_INT >= 26 && EaseCopyApplication.appContext.getApplicationInfo().targetSdkVersion > 22) {
            wmParams.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            PackageManager pm = getApplicationContext().getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", getPackageName()));
            if (permission || "Xiaomi".equals(Build.MANUFACTURER) || "vivo".equals(Build.MANUFACTURER)) {
                wmParams.type = LayoutParams.TYPE_PHONE;
            } else {
                wmParams.type = LayoutParams.TYPE_TOAST;
            }
        }
    }

    private void initClipboardContentView(LayoutInflater inflater) {
        //显示所有的需要复制到剪贴版的文本
        popupParams = new LayoutParams();
        popupParams.gravity = Gravity.CENTER;
        popupParams.width = LayoutParams.MATCH_PARENT;
        popupParams.height = LayoutParams.MATCH_PARENT;
        setVmType(popupParams);
        // 设置图片格式，效果为背景透明
        popupParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        popupParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        // 浮动窗口按钮
        mFloatWindow = (LinearLayout) inflater.inflate(
                R.layout.popup_clip_content, null);
        popupContent = mFloatWindow.findViewById(R.id.clip_content_view);
        mFloatWindow.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    private void setFlotWindowContent() {
        final List<ClipboardBean> result = new ArrayList<>();
        List<String> clipboardTexts = ClipboardUtil.getRecentClipboardTexts(getApplicationContext());
        if (clipboardTexts != null && clipboardTexts.size() > 0) {
            for (int i = 0; i < clipboardTexts.size(); i++) {
                ClipboardBean clipboardBean = new ClipboardBean();
                clipboardBean.setClipContent(clipboardTexts.get(i));
                clipboardBean.setDescription("最近使用");
                result.add(clipboardBean);
            }
        }

        ClipboardUtil.getAllRecords(new ClipboardUtil.GetDataDoneCallback() {
            @Override
            public void onGetDone(List<ClipboardDBModel> list) {
                List<ClipboardBean> clipboardBeans = ClipboardUtil.copyToClipBoardBean(list);
                result.addAll(result.size(), clipboardBeans);
            }
        });
        popupContent.setDataList(result);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            // 移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            mFloatLayout.setAlpha(0.4f);
        }
    }

}
