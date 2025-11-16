package com.save_help.Save_Help.helper.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperPostRequestDto {
    private Long helperId;
    private String title;
    private String content;
}