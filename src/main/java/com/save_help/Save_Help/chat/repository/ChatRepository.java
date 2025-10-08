package com.save_help.Save_Help.chat.repository;

import com.save_help.Save_Help.chat.entity.Chat;
import com.save_help.Save_Help.chat.entity.ChatRoom;
import com.save_help.Save_Help.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    // 두 유저 간의 채팅 내역 조회
    List<Chat> findBySenderAndReceiverOrderBySentAtAsc(User sender, User receiver);

    // 특정 유저가 받은 채팅 조회
    List<Chat> findByReceiverOrderBySentAtDesc(User receiver);

    // 특정 Call(화상통화/음성통화)과 연관된 채팅 조회
    List<Chat> findByCallIdOrderBySentAtAsc(Long callId);
}
