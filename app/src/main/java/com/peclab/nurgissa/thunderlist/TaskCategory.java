package com.peclab.nurgissa.thunderlist;


class TaskCategory {
    private int id;
    private String name;
    private int image;
    private int taskCount;
    private int imageColor;

    public TaskCategory() {}

    public TaskCategory(String name, int image, int imageColor) {
        this.name = name;
        this.image = image;
        this.imageColor = imageColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getImageColor() {
        return imageColor;
    }

    public void setImageColor(int imageColor) {
        this.imageColor = imageColor;
    }
}
