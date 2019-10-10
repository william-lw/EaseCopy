package com.william.androidsdk.utils;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String fileName = "no_content";
    private long fileSize;
    private String filePath;
    private boolean isInstalled;
    private Drawable iconId ;  //icon
    private int fileIcon;

    public int getFileIcon() {
        return fileIcon;
    }

    public void setFileIcon(int fileIcon) {
        this.fileIcon = fileIcon;
    }

    public Drawable getIconId() {
        return iconId;
    }

    public void setIconId(Drawable iconId) {
        this.iconId = iconId;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

}
