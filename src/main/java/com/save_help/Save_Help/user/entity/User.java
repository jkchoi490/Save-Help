package com.save_help.Save_Help.user.entity;

import com.save_help.Save_Help.helper.entity.HelperEmergencyContact;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(length = 255, unique = true)
    private String email;

    @Column(length = 255, unique = true)
    private String phone;

    @Column(length = 255)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Provider provider = Provider.LOCAL;

    @Column(name = "provider_user_id", nullable = false, length = 255)
    private String providerUserId;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    private String nickname;

    private String gender;

    private int age;

    private String incomeLevel;
    private boolean disabled;
    private boolean inEmergency;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HelperEmergencyContact> helperEmergencyContacts = new ArrayList<>();

    @Column(name = "total_donation_points")
    private int totalDonationPoints; // 누적 기부 포인트
}
