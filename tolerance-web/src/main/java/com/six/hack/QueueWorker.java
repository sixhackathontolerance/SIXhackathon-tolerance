package com.six.hack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class QueueWorker extends Thread {
    @Autowired
    private OutlierQueue queue;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ParticipantRepository repository;

    public QueueWorker() {
        start();
    }

    @Override
    public void run() {
        while (true) {
            if (queue == null) {
                sleep();
            } else {
                for (Price price : new PriceStream()) {
                    queue.offer(new Outlier(price));
                }
                break;
            }
        }
        while (true) {
            Outlier outlier = queue.next();
            if (outlier == null) {
                sleep();
            } else {
                boolean found = false;
                for (Participant participant : repository.getActiveSessions()) {
                    if (participant.ready()) {
                        participant.give(outlier);
                        messagingTemplate.convertAndSendToUser(participant.getName(), "/outlier", outlier);
                        System.out.println("queue worker handover task to user " + participant.getName());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    queue.offer(outlier);
                    System.out.println("queue worker no user found");
                    sleep();
                }
            }
        }
    }

    private void sleep() {
        System.out.println("queue worker sleep for 3 sec");
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("queue worker was interrupted");
        }
    }
}
