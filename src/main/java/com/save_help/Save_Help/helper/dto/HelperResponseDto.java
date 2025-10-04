package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.HelperRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HelperResponseDto {
    private Long id;
    private String name;
    private HelperRole role;
    private Long centerId;
    private Long hospitalId;
    private String phoneNumber;
    private boolean available;
}