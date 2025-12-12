package com.save_help.Save_Help.counseling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CounselingNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상담 1건당 노트 1개 (필요하면 1:N 리비전으로 확장 가능)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counseling_id", nullable = false, unique = true)
    private Counseling counseling;

    @Column(length = 4000)
    private String subjective;

    @Column(length = 4000)
    private String objective;

    @Column(length = 4000)
    private String assessment;

    @Column(length = 4000)
    private String plan;

    // 작성/수정 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") // Helper id
    private com.save_help.Save_Help.helper.entity.Helper author;

    private Boolean isFinalized; // 확정 여부 (확정 후 수정 제한/이력으로)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (isFinalized == null) isFinalized = false;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
