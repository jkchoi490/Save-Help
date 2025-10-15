package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDonation;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDonation.Status;
import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// ================= Request DTO =================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyNecessitiesDonationRequestDto {
    private String name;
    private NecessityCategory category;
    private String unit;
    private Integer quantity;
    private Long centerId; // CommunityCenter ID
    private Long donorId;  // User ID

    public DailyNecessitiesDonation toEntity(CommunityCenter center, User donor) {
        return new DailyNecessitiesDonation(name, category, unit, quantity, center, donor);
    }
}
