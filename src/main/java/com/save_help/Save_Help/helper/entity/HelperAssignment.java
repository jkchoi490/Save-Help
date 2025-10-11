package com.save_help.Save_Help.helper.entity;

import com.save_help.Save_Help.emergency.entity.Emergency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class HelperAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 Helper가 배정되었는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id", nullable = false)
    private Helper helper;

    // 어떤 긴급상황에 배정되었는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id", nullable = false)
    private Emergency emergency;

    // 배정 방식: AUTO(자동), MANUAL(수동)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentType assignmentType;

    // 배정된 시각
    @CreationTimestamp
    private LocalDateTime assignedAt;

    // 상태 (ACTIVE: 진행 중, CANCELLED: 취소됨, COMPLETED: 완료)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status = AssignmentStatus.ACTIVE;

    public HelperAssignment(Helper helper, Emergency emergency, AssignmentType assignmentType) {
        this.helper = helper;
        this.emergency = emergency;
        this.assignmentType = assignmentType;
        this.status = AssignmentStatus.ACTIVE;
    }

    public void markCompleted() {
        this.status = AssignmentStatus.COMPLETED;
    }

    public void cancel() {
        this.status = AssignmentStatus.CANCELLED;
    }
}
