package com.peclab.nurgissa.thunderlist;


class TaskCategory {
    private String title;
    private int image;
    private int itemCount;
    private int imageColor;

    public TaskCategory() {
    }

    public TaskCategory(String title, int image, int imageColor) {
        this.title = title;
        this.image = image;
        this.imageColor = imageColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getImageColor() {
        return imageColor;
    }

    public void setImageColor(int imageColor) {
        this.imageColor = imageColor;
    }
}
