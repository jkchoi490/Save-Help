package com.save_help.Save_Help.helper.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperNotificationDto {

    private Long id;
    private Long helperId;
    private String helperName;
    private String message;
    private LocalDateTime sentAt;
    private boolean success;
    private String errorReason;
}
