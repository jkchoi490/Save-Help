package com.save_help.Save_Help.emergency.entity;


import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.hospital.entity.Hospital;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requester;

    @Column(nullable = false, length = 500)
    private String description;


    private Double latitude;
    private Double longitude;


    @Enumerated(EnumType.STRING)
    private EmergencyStatus status;

    private boolean resolved;

    private LocalDateTime requestedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id")
    private Helper assignedHelper;

/*
    public void markResolved() {
        this.status = EmergencyStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = EmergencyStatus.CANCELLED;
        this.resolvedAt = LocalDateTime.now();
    }
*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital; // 해당 병원으로 배정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter assignedCenter;

    //긴급상황
    private String title;
    //위치(주소)
    private String location;
    //(긴급도)
    @Enumerated(EnumType.STRING)
    private EmergencySeverity severity;

    /** 헬퍼가 긴급 요청을 접수 */
    public void accept(Helper helper) {
        if (this.status != EmergencyStatus.REQUESTED) {
            throw new IllegalStateException("CREATED 상태에서만 접수할 수 있습니다.");
        }
        this.assignedHelper = helper;
        this.status = EmergencyStatus.ACCEPTED;
    }

    /** 관리자/센터가 헬퍼를 수동 배치 */
    public void assign(Helper helper) {
        if (this.status == EmergencyStatus.RESOLVED ||
                this.status == EmergencyStatus.CANCELLED) {
            throw new IllegalStateException("종료된 요청은 배치할 수 없습니다.");
        }
        this.assignedHelper = helper;
        this.status = EmergencyStatus.ASSIGNED;
    }

    /** 실제 대응 시작 */
    public void startProgress() {
        if (this.status != EmergencyStatus.ACCEPTED &&
                this.status != EmergencyStatus.ASSIGNED) {
            throw new IllegalStateException("ACCEPTED 또는 ASSIGNED 상태에서만 진행할 수 있습니다.");
        }
        this.status = EmergencyStatus.IN_PROGRESS;
    }

    /** 해결 완료 */
    public void markResolved() {
        if (this.status == EmergencyStatus.CANCELLED) {
            throw new IllegalStateException("취소된 요청은 해결 처리할 수 없습니다.");
        }
        this.status = EmergencyStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
        this.resolved = true;
    }

    /** 요청 취소 */
    public void cancel() {
        if (this.status == EmergencyStatus.RESOLVED) {
            throw new IllegalStateException("이미 해결된 요청은 취소할 수 없습니다.");
        }
        this.status = EmergencyStatus.CANCELLED;
        this.resolvedAt = LocalDateTime.now();
        this.resolved = false;
    }

}
