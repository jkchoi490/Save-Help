package com.save_help.Save_Help.counseling.entity;

import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_counseling_feedback_counseling", columnNames = {"counseling_id"})
        },
        indexes = {
                @Index(name = "idx_counseling_feedback_user", columnList = "user_id"),
                @Index(name = "idx_counseling_feedback_counselor", columnList = "counselor_id")
        }
)
public class CounselingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상담 1건당 피드백 1개
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counseling_id", nullable = false)
    private Counseling counseling;

    // 작성자(상담 요청자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 평가 대상(상담사)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private Helper counselor;

    // 상담사 평점 (1~5)
    private Integer counselorRating;

    // 전반 만족도 (1~5) (선택)
    private Integer sessionRating;

    @Column(length = 1000)
    private String comment;

    @Enumerated(EnumType.STRING)
    private CounselingFeedbackIssue issue;

    private LocalDateTime createdAt;

    protected CounselingFeedback() {}

    public CounselingFeedback(Counseling counseling, User user, Helper counselor,
                              Integer counselorRating, Integer sessionRating,
                              String comment, CounselingFeedbackIssue issue) {
        this.counseling = counseling;
        this.user = user;
        this.counselor = counselor;
        this.counselorRating = counselorRating;
        this.sessionRating = sessionRating;
        this.comment = comment;
        this.issue = (issue == null) ? CounselingFeedbackIssue.NONE : issue;
        this.createdAt = LocalDateTime.now();
    }
}
