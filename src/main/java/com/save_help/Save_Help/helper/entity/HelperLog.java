package com.save_help.Save_Help.helper.entity;

import com.save_help.Save_Help.emergency.entity.Emergency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class HelperLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;  // 긴급상황 배정 시각
    private LocalDateTime endTime;    // 처리 완료 시각
    private String action;            // 수행 내용 (예: 환자 이송, 상담, 진단 등)
    private String result;            // 결과 (성공/실패/미완료 등)
    private int satisfactionScore;    // 시민/관리자 평가 점수 (1~5)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id")
    private Helper helper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id")
    private Emergency emergency;
}
