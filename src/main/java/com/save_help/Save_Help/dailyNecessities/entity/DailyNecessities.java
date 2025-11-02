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


    // 품목명
    @Column(nullable = false, length = 100)
    private String name;

    // 카테고리
    @Enumerated(EnumType.STRING)
    private NecessityCategory category;

    // 단위
    @Column(nullable = false, length = 100)
    private String unit;

    // 보유 수량
    @Column(nullable = false)
    private Integer stock;

    //생필품 평점
    private Double averageRating;

    // 유효 기간
    private LocalDate expirationDate;

    // 제공 기관
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter providedBy;

    // 활성 여부
    private boolean active = true;

    // 승인 상태
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;


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
        this.approvalStatus = ApprovalStatus.PENDING;
    }
    public void deactivate() { this.active = false; }

    public void approve() {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.active = true;
    }

    public void reject() {
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.active = false;
    }

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }

}
