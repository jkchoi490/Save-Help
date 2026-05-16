package com.save_help.Save_Help.dailyNecessities.kafka.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesCreated {
    private Long necessityId;
    private String title;
    private String category;
    private Integer stock;
}
