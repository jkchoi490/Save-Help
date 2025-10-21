package com.save_help.Save_Help.helper.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class HelperReviewResponseDto {
    private Long reviewId;
    private Long helperId;
    private int rating;
    private String feedback;
    private LocalDateTime createdAt;
}