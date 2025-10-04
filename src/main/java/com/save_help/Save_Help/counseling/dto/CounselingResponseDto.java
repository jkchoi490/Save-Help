package com.save_help.Save_Help.counseling.dto;

import com.save_help.Save_Help.counseling.entity.CounselingStatus;
import com.save_help.Save_Help.counseling.entity.CounselingType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CounselingResponseDto {
    private Long id;
    private Long userId;
    private Long counselorId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private CounselingStatus status;
    private String notes;
    private CounselingType type;
}
