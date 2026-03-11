package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesNotification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DailyNecessitiesNotificationDto {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static DailyNecessitiesNotificationDto fromEntity(DailyNecessitiesNotification entity) {
        return DailyNecessitiesNotificationDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .type(entity.getType().name())
                .isRead(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}