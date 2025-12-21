package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DailyNecessitiesRequestResponseDto {

    private Long requestId;
    private Long userId;

    private Long centerId;

    private Long itemId;
    private String itemName;

    private Integer quantity;
    private String message;

    private String contact;
    private String address;

    private Integer priority;
    private DailyNecessitiesRequest.Status status;

    private String adminNote;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DailyNecessitiesRequestResponseDto from(DailyNecessitiesRequest r) {
        return  DailyNecessitiesRequestResponseDto.builder()
                .requestId(r.getId())
                .userId(r.getUser().getId())
                .centerId(r.getCenter() != null ? r.getCenter().getId() : null)
                .itemId(r.getItem() != null ? r.getItem().getId() : null)
                .itemName(r.getItem() != null ? r.getItem().getName() : r.getItemName())
                .quantity(r.getQuantity())
                .message(r.getMessage())
                .contact(r.getContact())
                .address(r.getAddress())
                .priority(r.getPriority())
                .status(r.getStatus())
                .adminNote(r.getAdminNote())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
