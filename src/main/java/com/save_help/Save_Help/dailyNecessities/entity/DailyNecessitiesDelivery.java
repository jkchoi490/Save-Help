package com.save_help.Save_Help.dailyNecessities.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientName; // 수령자 이름
    private String address;       // 배송 주소

    @Enumerated(EnumType.STRING)
    private DailyNecessitiesDeliveryStatus status; // 배송 상태
}