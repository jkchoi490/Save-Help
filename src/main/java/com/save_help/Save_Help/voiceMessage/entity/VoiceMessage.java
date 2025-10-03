package com.save_help.Save_Help.voiceMessage.entity;

import com.save_help.Save_Help.call.entity.Call;
import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class VoiceMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //보내는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    //받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    //음성 인식 파일 URL
    @Column(nullable = false, length = 500)
    private String fileUrl;

    //음성 파일 시간
    private Integer duration;

    //보낸 시각
    private LocalDateTime sentAt;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_id")
    private Call call;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counseling_id")
    private Counseling counseling;
}
