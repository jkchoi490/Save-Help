package com.save_help.Save_Help.emergency;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.hospital.entity.Hospital;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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


    private LocalDateTime requestedAt;

    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id")
    private Helper assignedHelper;


    public void markResolved() {
        this.status = EmergencyStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = EmergencyStatus.CANCELLED;
        this.resolvedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital; // 해당 병원으로 배정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter assignedCenter;
}
