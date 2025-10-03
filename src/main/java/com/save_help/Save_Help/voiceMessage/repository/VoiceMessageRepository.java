package com.save_help.Save_Help.voiceMessage.repository;

import com.save_help.Save_Help.voiceMessage.entity.VoiceMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceMessageRepository extends JpaRepository<VoiceMessage, Long> {
    List<VoiceMessage> findByReceiverIdAndReadFalse(Long receiverId);
}