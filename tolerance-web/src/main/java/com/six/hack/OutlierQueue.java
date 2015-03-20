package com.six.hack;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OutlierQueue {

    private BlockingQueue<Outlier> queue;

    public OutlierQueue() {
        queue = new LinkedBlockingQueue<Outlier>(50000);
    }

    public boolean offer(Outlier outlier) {
        return queue.offer(outlier);
    }

    public Outlier next() {
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }
}
