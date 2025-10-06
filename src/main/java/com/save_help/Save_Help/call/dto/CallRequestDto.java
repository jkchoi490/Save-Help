package com.save_help.Save_Help.call.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallRequestDto {
    private Long callerId; // 발신자
    private Long calleeId; // 수신자
}
