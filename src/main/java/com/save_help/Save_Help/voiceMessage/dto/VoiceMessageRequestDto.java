package com.save_help.Save_Help.voiceMessage.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
public class VoiceMessageRequestDto {
    private Long senderId;
    private Long receiverId;
    private String fileUrl;   // 저장된 음성 파일 경로
    private Integer duration; // 초 단위 길이
    private Long callId;
    private Long counselingId;
}

