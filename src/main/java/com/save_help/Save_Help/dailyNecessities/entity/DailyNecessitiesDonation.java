package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DailyNecessitiesDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private NecessityCategory category;

    private String unit;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommunityCenter center;

    @ManyToOne(fetch = FetchType.LAZY)
    private User donor;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime timestamp = LocalDateTime.now();

    public DailyNecessitiesDonation(String name, NecessityCategory category, String unit,
                                    Integer quantity, CommunityCenter center, User donor) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.quantity = quantity;
        this.center = center;
        this.donor = donor;
        this.status = Status.PENDING;
        this.timestamp = LocalDateTime.now();
    }

    public void approve() { this.status = Status.APPROVED; }

    public void reject() { this.status = Status.REJECTED; }

    public enum Status { PENDING, APPROVED, REJECTED }
}
