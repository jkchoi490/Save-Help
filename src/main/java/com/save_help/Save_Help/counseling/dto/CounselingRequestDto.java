package com.save_help.Save_Help.counseling.dto;

import com.save_help.Save_Help.counseling.entity.CounselingStatus;
import com.save_help.Save_Help.counseling.entity.CounselingType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CounselingRequestDto {
    private Long userId;
    private Long counselorId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private CounselingStatus status;
    private String notes;
    private CounselingType type;
}
