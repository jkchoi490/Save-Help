package com.save_help.Save_Help.userSubsidy.entity;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserSubsidy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private NationalSubsidy subsidy;

    private LocalDateTime grantedAt;
    private Integer amountGranted;
}
