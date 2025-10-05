package com.save_help.Save_Help.AmbulanceCall.dto;

import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCallStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "응급차 호출 DTO")
public class AmbulanceCallDto {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "요청자 ID", example = "10")
    private Long requesterId;

    @Schema(description = "긴급 상황 ID", example = "5")
    private Long emergencyId;

    @Schema(description = "호출 요청 시각", example = "2025-10-05T14:30:00")
    private LocalDateTime requestedAt;

    @Schema(description = "출발 시각", example = "2025-10-05T14:40:00")
    private LocalDateTime dispatchedAt;

    @Schema(description = "도착 시각", example = "2025-10-05T14:55:00")
    private LocalDateTime arrivedAt;

    @Schema(description = "상태", example = "REQUESTED")
    private AmbulanceCallStatus status;
}
