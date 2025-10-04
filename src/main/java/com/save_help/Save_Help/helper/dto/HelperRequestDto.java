package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.HelperRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelperRequestDto {
    private String name;
    private HelperRole role;
    private Long centerId;
    private Long hospitalId;
    private String phoneNumber;
    private boolean available;
}