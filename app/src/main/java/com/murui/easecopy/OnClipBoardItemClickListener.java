package com.murui.easecopy;

import com.murui.easecopy.bean.ClipboardBean;

public abstract class OnClipBoardItemClickListener {
    public void onClipItemClick(String text){};
    public void onEditButtonClick(ClipboardBean clipboardBean){};
    public void onDeleteButtonClick(){};
}