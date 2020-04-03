package com.wasteless.ui.home.goal;

import android.widget.ImageView;

public class SliderModel {
private Integer image;
private String title;
private String comment;
private String goal;
private Integer progress;


    public SliderModel(Integer image, String title, String comment, String goal, Integer progress) {
        this.image = image;
        this.title = title;
        this.comment = comment;
        this.goal = goal;
        this.progress = progress;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public int getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }


    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
