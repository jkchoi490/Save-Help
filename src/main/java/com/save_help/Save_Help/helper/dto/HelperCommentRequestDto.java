package com.save_help.Save_Help.helper.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperCommentRequestDto {
    private Long helperId;
    private String content;
}