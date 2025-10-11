package com.save_help.Save_Help.nationalSubsidy.entity;

import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubsidyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private NationalSubsidy subsidy;

    private LocalDate appliedDate = LocalDate.now();

    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
}
