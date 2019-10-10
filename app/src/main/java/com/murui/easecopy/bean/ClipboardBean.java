package com.murui.easecopy.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

public class ClipboardBean extends BaseObservable {
    private String tag;
    private long dateLong;
    private String dateTxt;
    private String clipContent;
    private String description;
    private boolean itemState;

    public ClipboardBean() {
    }

    public ClipboardBean(ClipboardDBModel bean) {
        this.tag = bean.getTag();
        this.dateLong = bean.getDateLong();
        this.dateTxt = bean.getDateTxt();
        this.clipContent = bean.getClipContent();
        this.description = bean.getDescription();
    }

    public String toString() {
        return "ClipboardBean{" +
                "tag='" + tag + '\'' +
                ", dateLong=" + dateLong +
                ", dateTxt='" + dateTxt + '\'' +
                ", clipContent='" + clipContent + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Bindable
    public boolean isItemState() {
        return itemState;
    }

    public ClipboardBean setItemState(boolean itemState) {
        this.itemState = itemState;
        notifyPropertyChanged(BR.itemState);
        return this;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public ClipboardBean setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
        return this;
    }

    @Bindable
    public String getTag() {
        return tag;
    }

    public ClipboardBean setTag(String tag) {
        this.tag = tag;
        notifyPropertyChanged(BR.tag);
        return this;
    }

    @Bindable
    public long getDateLong() {
        return dateLong;
    }

    public ClipboardBean setDateLong(long dateLong) {
        this.dateLong = dateLong;
        notifyPropertyChanged(BR.dateLong);
        return this;
    }

    @Bindable
    public String getDateTxt() {
        return dateTxt;
    }

    public ClipboardBean setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
        notifyPropertyChanged(BR.dateTxt);
        return this;
    }

    @Bindable
    public String getClipContent() {
        return clipContent;
    }

    public ClipboardBean setClipContent(String clipContent) {
        this.clipContent = clipContent;
        notifyPropertyChanged(BR.clipContent);
        return this;
    }
}
