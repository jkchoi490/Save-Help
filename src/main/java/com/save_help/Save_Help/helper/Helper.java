package com.save_help.Save_Help.helper;

import com.save_help.Save_Help.communityCenter.CommunityCenter;
import com.save_help.Save_Help.emergency.Emergency;
import com.save_help.Save_Help.hospital.entity.Hospital;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Helper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private HelperRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private CommunityCenter center;

    @Column(length = 20)
    private String phoneNumber;

    private boolean available = true;

    @OneToMany(mappedBy = "assignedHelper")
    private List<Emergency> emergencies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
