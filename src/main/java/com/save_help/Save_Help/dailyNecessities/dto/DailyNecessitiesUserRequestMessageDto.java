package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesUserRequestMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesUserRequestMessageDto {

    private Long id;
    private Long userId;
    private Long centerId;
    private String message;
    private LocalDateTime sentAt;
    private boolean processed;

    public static DailyNecessitiesUserRequestMessageDto fromEntity(DailyNecessitiesUserRequestMessage entity) {
        return DailyNecessitiesUserRequestMessageDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .centerId(entity.getCenter().getId())
                .message(entity.getMessage())
                .sentAt(entity.getSentAt())
                .processed(entity.isProcessed())
                .build();
    }
}