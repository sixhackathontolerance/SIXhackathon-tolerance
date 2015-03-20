package com.six.hack;

import org.springframework.beans.factory.annotation.Autowired;

public class ToleranceChecker extends Thread {

    @Autowired
    private OutlierQueue queue;

    @Override
    public void run() {
        super.run();
    }
}
