package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class DailyNecessities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private NecessityCategory category;

    private String unit;

    private Integer stock;

    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter providedBy;

    private boolean active = true;
}
