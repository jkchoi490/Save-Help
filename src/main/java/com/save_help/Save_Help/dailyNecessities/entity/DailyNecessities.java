package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private DailyNecessitiesCategory category;

    // 단위
    @Column(nullable = false, length = 100)
    private String unit;

    // 보유 수량
    @Column(nullable = false)
    private Integer stock;

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

    @Column(nullable = false)
    private int requestCount;

    //안전 재고
    private Integer safetyStock;

    //최대 재고
    private Integer maxStock;

    // 생필품 신청 기간
    private LocalDateTime applyStartedAt;
    private LocalDateTime applyEndedAt;

    // 지원 내용
    private String supportContents;

    // 생필품 상세 정보
    private String description;

    private Integer incomeLevel;

    // 신청 조건 충족 여부
    private Boolean requireCheck;


    // 기본 생성자
    protected DailyNecessities() {}

    // 생성자
    public DailyNecessities(String name, DailyNecessitiesCategory category, String unit,
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

    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    public void updateEligibilityCondition(Integer incomeLevel, Boolean requireCheck) {
        this.incomeLevel = incomeLevel;
        this.requireCheck = requireCheck;
    }

    public void updateSupportInfo(String supportContents, String description) {
        this.supportContents = supportContents;
        this.description = description;
    }

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }


    public boolean isAvailableForUser() {
        return active
                && approvalStatus == ApprovalStatus.APPROVED
                && stock != null
                && stock > 0;
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("추가 수량 관련 예외처리가 필요합니다");
        }
        if (this.stock == null) {
            this.stock = 0;
        }
        this.stock += quantity;
    }

    public void increaseRequestCount() {
        this.requestCount++;
    }

    public void updateApplyPeriod(LocalDateTime applyStartedAt, LocalDateTime applyEndedAt) {
        if (applyStartedAt != null && applyEndedAt != null && applyStartedAt.isAfter(applyEndedAt)) {
            throw new IllegalArgumentException("IllegalArgumentException");
        }
        this.applyStartedAt = applyStartedAt;
        this.applyEndedAt = applyEndedAt;
    }

    public boolean isWithinApplyPeriod() {
        LocalDateTime now = LocalDateTime.now();

        boolean started = (applyStartedAt == null) || !now.isBefore(applyStartedAt);
        boolean notEnded = (applyEndedAt == null) || !now.isAfter(applyEndedAt);

        return started && notEnded;
    }

    public boolean canAutoApply() {
        return active
                && approvalStatus == ApprovalStatus.APPROVED
                && stock != null
                && stock > 0
                && !isExpired()
                && isWithinApplyPeriod();
    }

    public String getAutoApplyReason() {
        return "생필품 자동 신청 조건 충족: " + this.name;
    }

    public boolean canAutoApplyWithQuantity(Integer quantity) {
        return canAutoApply();
    }

    public boolean hasEnoughStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return false;
        }

        return stock != null && stock >= quantity;
    }

    public void increaseStock(Integer quantity) {
        this.stock += quantity;
    }

    public boolean isAutoApplicableNow(Integer quantity) {
        return active
                && approvalStatus == ApprovalStatus.APPROVED
                && stock != null
                && quantity != null
                && quantity > 0
                && stock >= quantity
                && !isExpired()
                && isWithinApplyPeriod();
    }

    public boolean isStock() {
        return safetyStock != null
                && stock != null
                && stock <= safetyStock;
    }

    public boolean canIncreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return false;
        }

        if (maxStock == null) {
            return true;
        }

        return stock + quantity <= maxStock;
    }
    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("IllegalArgumentException");
        }

        if (stock == null || stock < quantity) {
            throw new IllegalStateException("IllegalArgumentException");
        }

        this.stock -= quantity;
    }
}
