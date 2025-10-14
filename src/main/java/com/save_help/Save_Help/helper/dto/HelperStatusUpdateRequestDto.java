package com.save_help.Save_Help.helper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelperStatusUpdateRequestDto {
    private boolean available; // true → 근무 가능 / false → 근무 불가
}