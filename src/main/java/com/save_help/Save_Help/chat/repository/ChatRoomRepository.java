package com.save_help.Save_Help.chat.repository;

import com.save_help.Save_Help.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}