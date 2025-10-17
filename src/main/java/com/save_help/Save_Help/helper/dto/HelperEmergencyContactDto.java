package com.save_help.Save_Help.helper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HelperEmergencyContactDto {
    private Long id;
    private Boolean read;
    private String message;
    private Long helperId;
    private Long userId;
}
