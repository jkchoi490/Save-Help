package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesCenterBoardDto {

    private Long centerId;           // 센터 ID
    private String centerName;       // 센터명

    private Long totalStock;         // 총 재고 수량
    private Long lowStockCount;      // 부족 품목 수

    private Long pendingRequests;    // 대기 중 요청 수
    private Long inProgressDeliveries; // 진행 중 배송 수
    private Long completedDeliveries;  // 완료된 배송 수
}