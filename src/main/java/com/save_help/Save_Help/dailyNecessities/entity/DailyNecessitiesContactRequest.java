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
public class DailyNecessitiesContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_necessities_id", nullable = false)
    private DailyNecessities dailyNecessities;

    @Column(nullable = false, length = 100)
    private String contactPhone;


    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactRequestStatus status = ContactRequestStatus.REQUESTED;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime processedAt;

    @Column(length = 1000)
    private String adminMemo;

    protected DailyNecessitiesContactRequest() {}

    public DailyNecessitiesContactRequest(
            User user,
            DailyNecessities dailyNecessities,
            String contactPhone,
            String message
    ) {
        this.user = user;
        this.dailyNecessities = dailyNecessities;
        this.contactPhone = contactPhone;
        this.message = message;
        this.status = ContactRequestStatus.REQUESTED;
        this.requestedAt = LocalDateTime.now();
    }

    public enum ContactRequestStatus {
        REQUESTED,
        IN_PROGRESS,
        COMPLETED,
        REJECTED
    }

    public void markInProgress() {
        this.status = ContactRequestStatus.IN_PROGRESS;
    }

    public void markCompleted(String adminMemo) {
        this.status = ContactRequestStatus.COMPLETED;
        this.adminMemo = adminMemo;
        this.processedAt = LocalDateTime.now();
    }

    public void reject(String adminMemo) {
        this.status = ContactRequestStatus.REJECTED;
        this.adminMemo = adminMemo;
        this.processedAt = LocalDateTime.now();
    }

    public CommunityCenter getTargetCenter() {
        return dailyNecessities.getProvidedBy();
    }
}