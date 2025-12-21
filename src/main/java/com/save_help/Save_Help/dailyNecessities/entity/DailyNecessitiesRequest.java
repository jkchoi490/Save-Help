package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "daily_necessity_request")
public class DailyNecessitiesRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 특정 센터 지정 (없으면 "전체 센터/서비스"로 브로드캐스트)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter center;

    // 특정 품목 지정(선택)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private DailyNecessities item;

    // 품목명(직접 입력 가능) - item이 없을 때 사용
    @Column(length = 100)
    private String itemName;

    // 요청 수량
    private Integer quantity;

    // 긴급 요청 사유/상황 설명
    @Column(nullable = false, length = 1000)
    private String message;

    // 위치/연락 등 추가 정보(선택)
    @Column(length = 300)
    private String contact;

    @Column(length = 200)
    private String address;

    // 우선순위(선택): 1(낮음)~5(높음)
    private Integer priority = 3;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status = Status.REQUESTED;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    // 센터/관리자 처리 메모(거절 사유, 안내 등)
    @Column(length = 500)
    private String adminNote;

    public enum Status {
        REQUESTED,   // 접수
        ASSIGNED,    // 센터 배정 완료
        IN_PROGRESS, // 처리 중
        FULFILLED,   // 지원 완료
        REJECTED,    // 거절
        CANCELLED    // 사용자 취소
    }
}
