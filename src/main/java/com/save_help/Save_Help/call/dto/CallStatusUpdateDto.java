package com.save_help.Save_Help.call.dto;

import com.save_help.Save_Help.call.entity.CallStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallStatusUpdateDto {
    private CallStatus status; // ACCEPTED, REJECTED, ENDED
}
