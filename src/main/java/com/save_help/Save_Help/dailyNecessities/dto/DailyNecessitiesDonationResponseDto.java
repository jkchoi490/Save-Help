package com.save_help.Save_Help.dailyNecessities.dto;


import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDonation;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyNecessitiesDonationResponseDto {
    private Long id;
    private String name;
    private NecessityCategory category;
    private String unit;
    private Integer quantity;
    private Long centerId;
    private String centerName;
    private Long donorId;
    private String donorName;
    private DailyNecessitiesDonation.Status status;
    private LocalDateTime timestamp;

    public static DailyNecessitiesDonationResponseDto fromEntity(DailyNecessitiesDonation donation) {
        return new DailyNecessitiesDonationResponseDto(
                donation.getId(),
                donation.getName(),
                donation.getCategory(),
                donation.getUnit(),
                donation.getQuantity(),
                donation.getCenter() != null ? donation.getCenter().getId() : null,
                donation.getCenter() != null ? donation.getCenter().getName() : null,
                donation.getDonor() != null ? donation.getDonor().getId() : null,
                donation.getDonor() != null ? donation.getDonor().getName() : null,
                donation.getStatus(),
                donation.getTimestamp()
        );
    }
}