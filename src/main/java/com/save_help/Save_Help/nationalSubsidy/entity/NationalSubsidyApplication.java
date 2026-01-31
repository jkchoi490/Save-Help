package com.save_help.Save_Help.nationalSubsidy.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "national_subsidy_application",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_subsidy",
                columnNames = {"user_id", "subsidy_id"}
        ),
        indexes = {
                @Index(name = "idx_app_user", columnList = "user_id"),
                @Index(name = "idx_app_subsidy", columnList = "subsidy_id"),
                @Index(name = "idx_app_status", columnList = "status")
        }
)
public class NationalSubsidyApplication {

    public enum Status { APPLIED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subsidy_id")
    private NationalSubsidy subsidy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(length = 1000)
    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean active = true;
}
