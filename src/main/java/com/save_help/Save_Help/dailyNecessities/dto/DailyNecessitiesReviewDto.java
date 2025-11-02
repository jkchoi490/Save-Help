package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesReview;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyNecessitiesReviewDto {
    private Long id;
    private Long userId;
    private Long itemId;
    private int rating;
    private String comment;

    public static DailyNecessitiesReviewDto fromEntity(DailyNecessitiesReview r) {
        return DailyNecessitiesReviewDto.builder()
                .id(r.getId())
                .userId(r.getReviewer().getId())
                .itemId(r.getItem().getId())
                .rating(r.getRating())
                .comment(r.getComment())
                .build();
    }
}
