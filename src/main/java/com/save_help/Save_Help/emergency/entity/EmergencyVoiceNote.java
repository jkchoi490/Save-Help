package com.save_help.Save_Help.emergency.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class EmergencyVoiceNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id", nullable = false)
    private Emergency emergency;

    @Column(nullable = false, length = 2000)
    private String transcript;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoiceInputSource source = VoiceInputSource.CLIENT_STT;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (source == null) source = VoiceInputSource.CLIENT_STT;
    }
}

