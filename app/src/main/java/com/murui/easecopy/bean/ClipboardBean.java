package com.murui.easecopy.bean;

public class ClipboardBean {
    private String tag;
    private long dateLong;
    private String dateTxt;
    private String clipContent;
    private String description;

    public String getDescription() {
        return description;
    }

    public ClipboardBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public ClipboardBean setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public long getDateLong() {
        return dateLong;
    }

    public ClipboardBean setDateLong(long dateLong) {
        this.dateLong = dateLong;
        return this;
    }

    public String getDateTxt() {
        return dateTxt;
    }

    public ClipboardBean setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
        return this;
    }

    public String getClipContent() {
        return clipContent;
    }

    public ClipboardBean setClipContent(String clipContent) {
        this.clipContent = clipContent;
        return this;
    }
}
