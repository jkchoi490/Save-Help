package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserNecessityRequestResponseDto {
    private Long id;
    private String userName;
    private String itemName;
    private Integer quantity;
    private String status;
    private LocalDateTime timestamp;

    public UserNecessityRequestResponseDto(UserNecessityRequest request) {
        this.id = request.getId();
        this.userName = request.getUser().getName();
        this.itemName = request.getItem().getName();
        this.quantity = request.getQuantity();
        this.status = request.getStatus().name();
        this.timestamp = request.getRequestedAt();
    }

    public static UserNecessityRequestResponseDto fromEntity(UserNecessityRequest request) {
        return new UserNecessityRequestResponseDto(request);
    }
}