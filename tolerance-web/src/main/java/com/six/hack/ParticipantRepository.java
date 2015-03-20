package com.six.hack;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Sergi Almar
 */
public class ParticipantRepository {
    private Map<String, Participant> activeSessions = new ConcurrentHashMap<>();

    public void add(String sessionId, Participant participan) {
        activeSessions.put(sessionId, participan);
    }

    public Participant getParticipant(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public void removeParticipant(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public Collection<Participant> getActiveSessions() {
        return activeSessions.values();
    }
}
