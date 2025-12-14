package com.save_help.Save_Help.transportationCall.entity;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.transportationCall.entity.TransportationCall;
import com.save_help.Save_Help.transportationCall.entity.Vehicle;
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
                // 호출 1건당 피드백 1개
                @UniqueConstraint(name = "uk_feedback_call", columnNames = {"call_id"})
        },
        indexes = {
                @Index(name = "idx_feedback_driver", columnList = "driver_id"),
                @Index(name = "idx_feedback_vehicle", columnList = "vehicle_id"),
                @Index(name = "idx_feedback_requester", columnList = "requester_id")
        }
)
public class TransportationFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 호출에 대한 피드백인지
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id", nullable = false)
    private TransportationCall call;

    // 작성자(호출 요청자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // 평가 대상: 운전자(Helper)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Helper driver;

    // 평가 대상: 차량
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    // 운전자 평점(1~5)
    private Integer driverRating;

    // 차량 평점(1~5)
    private Integer vehicleRating;

    // 공통 코멘트
    @Column(length = 1000)
    private String comment;

    @Enumerated(EnumType.STRING)
    private TransportationFeedbackIssue issue; // 선택: 불만/이슈 유형

    private LocalDateTime createdAt;

    protected TransportationFeedback() {}

    public TransportationFeedback(TransportationCall call, User requester, Helper driver, Vehicle vehicle,
                                  Integer driverRating, Integer vehicleRating, String comment,
                                  TransportationFeedbackIssue issue) {
        this.call = call;
        this.requester = requester;
        this.driver = driver;
        this.vehicle = vehicle;
        this.driverRating = driverRating;
        this.vehicleRating = vehicleRating;
        this.comment = comment;
        this.issue = issue;
        this.createdAt = LocalDateTime.now();
    }
}
