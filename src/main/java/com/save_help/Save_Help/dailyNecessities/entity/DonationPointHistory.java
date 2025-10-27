package com.save_help.Save_Help.dailyNecessities.entity;


import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationPointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User donor; // 기부자

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private DailyNecessitiesDonation donation; // 관련 기부 내역

    private int points; // 적립된 포인트
    private LocalDateTime createdAt; // 적립 시각

}