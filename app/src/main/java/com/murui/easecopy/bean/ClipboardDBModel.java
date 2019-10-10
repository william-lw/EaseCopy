package com.murui.easecopy.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class ClipboardDBModel extends BaseModel {
    @Column
    private String tag = "tag";

    @Column
    @PrimaryKey
    private long dateLong;

    @Column
    private String dateTxt;

    @Column
    private String clipContent;

    @Column
    private String description;

    @Override
    public String toString() {
        return "ClipboardBean{" +
                "tag='" + tag + '\'' +
                ", dateLong=" + dateLong +
                ", dateTxt='" + dateTxt + '\'' +
                ", clipContent='" + clipContent + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    public String getDescription() {
        return description;
    }

    public ClipboardDBModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public ClipboardDBModel setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public long getDateLong() {
        return dateLong;
    }

    public ClipboardDBModel setDateLong(long dateLong) {
        this.dateLong = dateLong;
        return this;
    }

    public String getDateTxt() {
        return dateTxt;
    }

    public ClipboardDBModel setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
        return this;
    }

    public String getClipContent() {
        return clipContent;
    }

    public ClipboardDBModel setClipContent(String clipContent) {
        this.clipContent = clipContent;
        return this;
    }
}
