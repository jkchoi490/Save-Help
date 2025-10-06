package com.save_help.Save_Help.call.dto;

import com.save_help.Save_Help.call.entity.CallStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CallResponseDto {
    private Long id;
    private Long callerId;
    private Long calleeId;
    private CallStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
