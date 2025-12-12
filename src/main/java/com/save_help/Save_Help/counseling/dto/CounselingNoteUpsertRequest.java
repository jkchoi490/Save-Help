package com.save_help.Save_Help.counseling.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CounselingNoteUpsertRequest {
    private String subjective;
    private String objective;
    private String assessment;
    private String plan;

    // 선택: 확정 저장 버튼에서 true로
    private Boolean finalize;
}