package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.HelperFeedback;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperFeedbackDto {

    private Long id;
    private Long helperId;
    private String helperName;
    private String title;
    private String content;
    private String status;
    private String adminResponse;

    public static HelperFeedbackDto fromEntity(HelperFeedback feedback) {
        return HelperFeedbackDto.builder()
                .id(feedback.getId())
                .helperId(feedback.getHelper().getId())
                .helperName(feedback.getHelper().getName())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .status(feedback.getStatus().name())
                .adminResponse(feedback.getAdminResponse())
                .build();
    }
}
