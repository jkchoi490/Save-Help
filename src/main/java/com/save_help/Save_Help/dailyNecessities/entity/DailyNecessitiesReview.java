package com.save_help.Save_Help.dailyNecessities.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DailyNecessitiesReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewer; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    private DailyNecessities item; // 리뷰 대상 품목

    @Column(nullable = false)
    private int rating; // 1~5 점수

    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();
}
