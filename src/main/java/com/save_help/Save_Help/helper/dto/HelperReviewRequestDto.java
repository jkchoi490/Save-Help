package com.save_help.Save_Help.helper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelperReviewRequestDto {
    private Long helperId;
    private Long emergencyId;
    private int rating; // 1~5
    private String feedback;
}