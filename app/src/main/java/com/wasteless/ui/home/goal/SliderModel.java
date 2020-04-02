package com.wasteless.ui.home.goal;

public class SliderModel {
private String title;
private String comment;
private String goal;
    public SliderModel(String title, String comment, String goal) {
        this.title = title;
        this.comment = comment;
        this.goal = goal;
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
