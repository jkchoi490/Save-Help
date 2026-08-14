package com.save_help.Save_Help.dailyNecessities.kafka.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DailyNecessitiesCreatedEvent {
    private Long eventId; // ID
    private Long necessityId; //생필품 ID
    private String title; // 생필품 이름
    private String category; // 생필품 분류(카테고리)
    private Integer stock; // 생필품 개수
    LocalDateTime occurredAt;
}
