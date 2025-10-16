package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesUserRequestMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 요청할 센터
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter center;

    @Column(length = 500)
    private String message; // 요청 내용 (품목명, 수량 등)

    private LocalDateTime sentAt = LocalDateTime.now();

    private boolean processed = false; // 센터 처리 여부
}
