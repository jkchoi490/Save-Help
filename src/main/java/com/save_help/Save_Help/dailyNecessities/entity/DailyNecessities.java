package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

    // 기본 생성자
    protected DailyNecessities() {}

    // 생성자
    public DailyNecessities(String name, NecessityCategory category, String unit,
                            Integer stock, LocalDate expirationDate, CommunityCenter providedBy) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.stock = stock;
        this.expirationDate = expirationDate;
        this.providedBy = providedBy;
        this.active = true;
    }
    public void deactivate() { this.active = false; }
}
