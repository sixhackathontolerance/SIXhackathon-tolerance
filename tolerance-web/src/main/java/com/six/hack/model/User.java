package com.six.hack.model;


public class User {
    private String name;
    private Outlier outlier;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean ready() {
        return outlier == null;
    }

    public void give(Outlier outlier) {
        this.outlier = outlier;
    }

    public void idle() {
        this.outlier = null;
    }
}
