package com.save_help.Save_Help.nationalSubsidy.entity;

import com.save_help.Save_Help.nationalSubsidy.kafka.ApplicationStatus;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "subsidy_application",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_subsidy", columnNames = {"user_id", "subsidy_id"})
        },
        indexes = {
                @Index(name = "idx_subsidy_application_user", columnList = "user_id"),
                @Index(name = "idx_subsidy_application_subsidy", columnList = "subsidy_id")
        }
)
public class SubsidyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subsidy_id")
    private NationalSubsidy subsidy;

    private LocalDate appliedDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=16)
    private ApplicationStatus status;


    @CreationTimestamp
    private LocalDateTime createdAt;
}
