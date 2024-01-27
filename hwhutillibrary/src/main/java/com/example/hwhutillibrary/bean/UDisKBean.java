package com.example.hwhutillibrary.bean;

public class UDisKBean {
    private String name;
    private String status;
    private boolean isEmulated;
    private boolean isRemovable;
    private String mPath;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEmulated() {
        return isEmulated;
    }

    public void setEmulated(boolean emulated) {
        isEmulated = emulated;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(boolean removable) {
        isRemovable = removable;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }
}
