package com.murui.easecopy;

import com.murui.easecopy.bean.ClipboardBean;

public interface OnFloatWindowClickListener {
    void onClipItemClick(ClipboardBean clipboardBean);
    void onOutsideClick();
}