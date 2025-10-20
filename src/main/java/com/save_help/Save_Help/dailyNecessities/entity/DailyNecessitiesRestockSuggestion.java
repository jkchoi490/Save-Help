package com.save_help.Save_Help.dailyNecessities.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DailyNecessitiesRestockSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 생필품인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "necessity_id", nullable = false)
    private DailyNecessities necessity;

    // 예측된 부족 시점
    private LocalDateTime predictedOutOfStockDate;

    // 제안 생성일
    private LocalDateTime createdAt;

    // 제안 상태: PENDING / APPROVED / REJECTED
    @Enumerated(EnumType.STRING)
    private SuggestionStatus status;

    public enum SuggestionStatus {
        PENDING, APPROVED, REJECTED
    }
}
