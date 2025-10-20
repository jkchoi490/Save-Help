package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.Helper;
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

    public HelperResponseDto(Helper helper) {
        this.id = helper.getId();
        this.name = helper.getName();
        this.role = helper.getRole();
        this.centerId = helper.getCommunityCenter() != null ? helper.getCommunityCenter().getId() : null;
        this.hospitalId = helper.getHospital() != null ? helper.getHospital().getId() : null;
        this.phoneNumber = helper.getPhoneNumber();
        this.available = helper.isAvailable();
    }
}