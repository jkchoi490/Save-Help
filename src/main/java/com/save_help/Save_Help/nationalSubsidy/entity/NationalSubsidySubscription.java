package com.save_help.Save_Help.nationalSubsidy.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "national_subsidy_subscription")
public class NationalSubsidySubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구독한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // === 구독 조건들 ===
    private Integer minAge;
    private Integer maxAge;

    private String incomeLevel;

    private Boolean disability;
    private Boolean emergency;

    @Enumerated(EnumType.STRING)
    private SubsidyType type;

    // 지역(센터 기준 or 주소 기준)
    @Column(length = 100)
    private String region;

    // 활성화 여부 (구독 일시 중지 가능)
    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
