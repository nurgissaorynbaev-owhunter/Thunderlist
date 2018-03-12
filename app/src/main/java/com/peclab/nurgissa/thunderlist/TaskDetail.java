package com.peclab.nurgissa.thunderlist;


public class TaskDetail {
    public static final int EDIT_VALUE = 0;
    public static final int SCHEDULE = 1;
    public static final int NOTE = 2;
    public static final int ADD_SUBTASK = 3;
    public static final int SUBTASK = 4;
    public static final int EDIT_VALUE_IMAGE = R.drawable.ic_subject_white_24dp;
    public static final int SCHEDULE_IMAGE = R.drawable.ic_event_white_24dp;
    public static final int NOTE_IMAGE = R.drawable.ic_mode_edit_white_24dp;
    public static final int ADD_SUBTASK_IMAGE = R.drawable.ic_add_white_24dp;

    private int id;
    private String text;
    private int image;
    private int viewType;

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
}
