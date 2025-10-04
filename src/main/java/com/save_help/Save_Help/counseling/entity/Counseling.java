package com.save_help.Save_Help.counseling.entity;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Counseling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상담을 요청한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 상담사 (Helper 엔티티에서 상담사 역할)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id")
    private Helper counselor;

    // 상담 시작 시간
    private LocalDateTime startAt;

    // 상담 종료 시간
    private LocalDateTime endAt;

    // 상담 상태
    @Enumerated(EnumType.STRING)
    private CounselingStatus status;

    // 상담 내용 (선택: 상담 기록)
    @Column(length = 2000)
    private String notes;

    // 상담 유형 (전화, 화상, 대면 등)
    @Enumerated(EnumType.STRING)
    private CounselingType type;
}
