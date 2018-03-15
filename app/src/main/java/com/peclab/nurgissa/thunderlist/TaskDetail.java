package com.peclab.nurgissa.thunderlist;


public class TaskDetail {
    public static final int VIEW_TYPE_EDIT_VALUE = 0;
    public static final int VIEW_TYPE_SCHEDULE = 1;
    public static final int VIEW_TYPE_NOTE = 2;
    public static final int VIEW_TYPE_ADD_SUBTASK = 3;
    public static final int VIEW_TYPE_SUBTASK = 4;

    public static final int IMAGE_EDIT_VALUE = R.drawable.ic_subject_white_24dp;
    public static final int IMAGE_SCHEDULE = R.drawable.ic_event_white_24dp;
    public static final int IMAGE_NOTE = R.drawable.ic_mode_edit_white_24dp;
    public static final int IMAGE_ADD_SUBTASK = R.drawable.ic_add_white_24dp;

    private int id;
    private String text;
    private int image;
    private int viewType;
    private boolean isHint;
    private int imageColor;
    private int textColor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isHint() {
        return isHint;
    }

    public void setHint(boolean hint) {
        isHint = hint;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getImageColor() {
        return imageColor;
    }

    public void setImageColor(int imageColor) {
        this.imageColor = imageColor;
    }
}
