package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNecessityRequestDto {
    private Long userId;
    private Long itemId;
    private Integer quantity;
}