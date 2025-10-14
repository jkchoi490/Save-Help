package com.save_help.Save_Help.helper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class HelperAssignmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long helperId;
    private Long emergencyId;
    private String action; // ASSIGNED, UNASSIGNED, REASSIGNED
    private LocalDateTime actionAt;
    private String reason;
}