package com.six.hack.model;

public class User {
    private String name;
    private int verified;
    private boolean ready = true;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isReady() {
        return ready;
    }

    public int getVerified() {
        return verified;
    }

    public void give(Outlier outlier) {
        ready = false;
    }

    public void idle() {
        verified++;
        ready = true;
    }
}
