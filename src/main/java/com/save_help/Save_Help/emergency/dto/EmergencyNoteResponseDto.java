package com.save_help.Save_Help.emergency.dto;

import java.time.LocalDateTime;

public record EmergencyNoteResponseDto(
        Long id,
        Long writerId,
        String content,
        LocalDateTime createdAt
) {}