package com.six.hack;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.six.hack.model.Outlier;
import com.six.hack.model.User;
import com.six.hack.model.Verified;

@Controller
public class WebController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private BlockingQueue<Outlier> queue = new LinkedBlockingQueue<Outlier>(50000);
    private Map<String, User> users = new ConcurrentHashMap<String, User>();

    @MessageMapping("/verified")
    public void done(Verified verified, Principal principal) throws Exception {
        users.get(principal.getName()).idle();
        System.out.println("controller set " + principal.getName() + " to idle");
    }

    public void toQueue(Outlier outlier) {
        queue.offer(outlier);
    }

    public Outlier next() {
        return queue.poll();
    }

    public Collection<User> users() {
        return users.values();
    }

    public void toUser(User user, Outlier outlier) {
        user.give(outlier);

        messagingTemplate.convertAndSendToUser(user.getName(), "/outlier", outlier);
        System.out.println("queue worker handover task to user " + user.getName());
        messagingTemplate.convertAndSend("/topic/queuesize", "{\"size\": " + queue.size() + "}");
    }

    public boolean queueEmpty() {
        return queue.isEmpty();
    }

    public void login(String username) {
        users.put(username, new User(username));
        System.out.println("login user " + username);
    }

    public void logout(String username) {
        User user = users.get(username);
        if (user == null) {
            System.err.println("logout but no user found " + username);
        } else {
            users.remove(username);
            System.out.println("logout user " + username);
        }
    }
}
