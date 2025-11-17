package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.HelperRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminNoticeRequestDto {

    private String title;       // 공지 제목
    private String message;     // 공지 내용

    private HelperRole role;    // 특정 역할 대상 (선택)
    private Long centerId;      // 특정 센터 대상 (선택)

    private boolean sendToAll;  // 전체 헬퍼에게 발송할지 여부
}