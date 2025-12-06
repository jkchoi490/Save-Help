package com.save_help.Save_Help.counseling.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignalingMessage {

    private String type;  // "offer", "answer", "candidate", "join", "leave"
    private Object data;  // SDP or ICE candidate 정보
    private String senderId;  // optional: 누가 보냈는지 구분
}