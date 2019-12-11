package com.fstack.RecoveryPhoto.Pojo;

public class ImageData {
    String path;
    boolean isSelcted;

    public ImageData(String path, boolean isSelected) {
        this.path = path;
        this.isSelcted = isSelected;
    }

    public String getFilePath() {
        return this.path;
    }

    public void setFilePath(String str) {
        this.path = str;
    }

    public boolean getSelection() {
        return this.isSelcted;
    }

    public void setSelction(boolean z) {
        this.isSelcted = z;
    }
}
