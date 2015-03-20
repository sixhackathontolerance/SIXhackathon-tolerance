package com.six.hack;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {
    @Autowired
    private ParticipantRepository userRepository;

    @MessageMapping("/done")
    public void done(Outlier outlier, Principal principal) throws Exception {
        userRepository.getParticipant(principal.getName()).idle();
        System.out.println("controller set " + principal.getName() + " to idle");
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message, Principal principal) throws Exception {
        userRepository.getParticipant(principal.getName()).idle();
        System.out.println("controller set " + principal.getName() + " to idle");
        return new Greeting("Hello, " + message.getName() + "! .. " + principal.getName());
    }
}
