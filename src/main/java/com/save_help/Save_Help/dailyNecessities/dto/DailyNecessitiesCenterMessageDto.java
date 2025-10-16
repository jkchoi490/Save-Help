package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCenterMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesCenterMessageDto {
    private Long id;
    private Long centerId;
    private Long userId;
    private String message;
    private LocalDateTime sentAt;
    private boolean read;

    public static DailyNecessitiesCenterMessageDto fromEntity(DailyNecessitiesCenterMessage msg) {
        return DailyNecessitiesCenterMessageDto.builder()
                .id(msg.getId())
                .centerId(msg.getCenter().getId())
                .userId(msg.getUser().getId())
                .message(msg.getMessage())
                .sentAt(msg.getSentAt())
                .read(msg.isRead())
                .build();
    }

}
