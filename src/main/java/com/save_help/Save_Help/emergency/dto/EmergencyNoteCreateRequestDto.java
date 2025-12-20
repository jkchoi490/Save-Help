package com.save_help.Save_Help.emergency.dto;

public record EmergencyNoteCreateRequestDto(
        Long writerId,
        String content
) {}