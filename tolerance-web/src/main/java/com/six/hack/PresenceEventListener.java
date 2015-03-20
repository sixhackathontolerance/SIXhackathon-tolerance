package com.six.hack;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class PresenceEventListener implements ApplicationListener<ApplicationEvent> {

    private ParticipantRepository participantRepository;

    public PresenceEventListener(SimpMessagingTemplate messagingTemplate, ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof SessionConnectEvent) {
            handleSessionConnected((SessionConnectEvent) event);
        } else if (event instanceof SessionDisconnectEvent) {
            handleSessionDisconnect((SessionDisconnectEvent) event);
        }
    }

    private void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        // participantRepository.add(headers.getSessionId(), new
        // Participant(username));
        participantRepository.add(username, new Participant(username));
        System.out.println("login user " + username);
    }

    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        Participant participant = participantRepository.getParticipant(event.getSessionId());
        if (participant == null) {
            System.err.println("logout but no user found for session " + event.getSessionId());
        } else {
            participantRepository.removeParticipant(participant.getName());
            System.out.println("logout user " + participant.getName());
        }
    }
}
