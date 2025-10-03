package com.save_help.Save_Help.voiceMessage.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
public class VoiceMessageResponseDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String fileUrl;
    private Integer duration;
    private LocalDateTime sentAt;
    private boolean read;
    private String transcript; // 음성 인식된 텍스트
}