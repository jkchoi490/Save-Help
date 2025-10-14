package com.save_help.Save_Help.helper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class HelperSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 근무 시작/종료 시간
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // 근무 상태 (예: 예정, 진행중, 완료, 휴무)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id")
    private Helper helper;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    // 근무 장소 (센터, 병원)
    private String location;

    // 비고 (예: 야간근무, 대체근무 등)
    private String note;
}
