package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestReasonResponseDto {
    private Long requestId;
    private Long userId;
    private Long itemId;
    private String reason;
}