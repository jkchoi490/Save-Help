package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.HelperActivityStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelperActivityUpdateRequestDto {
    private HelperActivityStatus status;
}
