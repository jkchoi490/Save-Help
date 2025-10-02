package com.save_help.Save_Help.voiceMessage.entity;

import com.save_help.Save_Help.call.entity.Call;
import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VoiceMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(nullable = false, length = 500)
    private String fileUrl;

    private Integer duration;

    private LocalDateTime sentAt;

    private boolean read = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id")
    private Call call;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counseling_id")
    private Counseling counseling;
}
