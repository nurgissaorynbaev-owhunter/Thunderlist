package com.peclab.nurgissa.thunderlist;


public class DetailTask {
    private int image;
    private String hint;

    public DetailTask(int image, String hint) {
        this.image = image;
        this.hint = hint;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return hint;
    }
}
