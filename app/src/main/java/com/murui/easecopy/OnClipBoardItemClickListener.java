package com.murui.easecopy;

import com.murui.easecopy.bean.ClipboardBean;

public interface  OnClipBoardItemClickListener {
    public void onClipItemClick(ClipboardBean clipboardBean);
    public void onEditButtonClick(ClipboardBean clipboardBean);
    public void onDeleteButtonClick(ClipboardBean clipboardBean);
}