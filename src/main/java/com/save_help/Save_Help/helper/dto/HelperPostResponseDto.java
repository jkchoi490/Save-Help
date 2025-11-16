package com.save_help.Save_Help.helper.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperPostResponseDto {
    private Long id;
    private String authorName;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<HelperCommentResponseDto> comments;
}