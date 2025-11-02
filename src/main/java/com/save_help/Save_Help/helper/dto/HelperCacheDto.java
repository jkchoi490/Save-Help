package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.HelperActivityStatus;
import com.save_help.Save_Help.helper.entity.HelperRole;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperCacheDto implements Serializable {
    private Long id;
    private String name;
    private HelperRole role;
    private boolean available;
    private HelperActivityStatus activityStatus;
    private Double latitude;
    private Double longitude;
    private Double trustScore;
    private LocalDateTime lastLocationUpdateTime;
}
