package com.save_help.Save_Help.counseling.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CounselingNoteResponse {
    private Long id;
    private Long counselingId;

    private String subjective;
    private String objective;
    private String assessment;
    private String plan;

    private Long authorId;
    private Boolean finalized;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}