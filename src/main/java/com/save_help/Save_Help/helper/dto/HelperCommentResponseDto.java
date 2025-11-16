package com.save_help.Save_Help.helper.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperCommentResponseDto {
    private Long id;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
}