package com.save_help.Save_Help.emergency.dto;

import jakarta.validation.constraints.NotNull;

public record EmergencyAcceptRequestDto (
    @NotNull(message = "helperId는 필수입니다.")
    Long helperId
){}
