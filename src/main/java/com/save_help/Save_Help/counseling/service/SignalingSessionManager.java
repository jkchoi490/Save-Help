package com.save_help.Save_Help.counseling.service;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SignalingSessionManager {

    private final Map<String, Set<String>> sessionUsers = new HashMap<>();

    // 유저 방 입장
    public void joinSession(String sessionId, String userId) {
        sessionUsers.computeIfAbsent(sessionId, k -> new HashSet<>()).add(userId);
    }

    // 유저 방 퇴장
    public void leaveSession(String sessionId, String userId) {
        if (sessionUsers.containsKey(sessionId)) {
            sessionUsers.get(sessionId).remove(userId);
        }
    }

    // 유저가 방에 있는지 체크
    public boolean isUserInSession(String sessionId, String userId) {
        return sessionUsers.containsKey(sessionId) && sessionUsers.get(sessionId).contains(userId);
    }

    // 방에 있는 모든 유저 조회
    public Set<String> getUsersInSession(String sessionId) {
        return sessionUsers.getOrDefault(sessionId, Collections.emptySet());
    }
}
