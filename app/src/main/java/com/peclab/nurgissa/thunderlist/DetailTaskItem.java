package com.peclab.nurgissa.thunderlist;


public class DetailTaskItem {
    public static final int VALUE = 0;
    public static final int ADD_SUBTASK = 3;
    public static final int SUBTASK = 4;
    public static final int SCHEDULE = 1;
    public static final int NOTE = 2;
    private int image;
    private String text;
    private int viewType;

    public DetailTaskItem() {}

    public DetailTaskItem(int image, String text, int viewType) {
        this.image = image;
        this.text = text;
        this.viewType = viewType;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public String toString() {
        return text;
    }
}
