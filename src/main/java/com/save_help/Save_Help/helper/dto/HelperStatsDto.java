package com.save_help.Save_Help.helper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelperStatsDto {
    private Long helperId;
    private String helperName;
    private Long totalAssigned;
    private Double avgResponseMinutes;
    private LocalDateTime lastAssignedAt;
}