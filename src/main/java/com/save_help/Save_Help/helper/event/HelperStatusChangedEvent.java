package com.save_help.Save_Help.helper.event;

import com.save_help.Save_Help.helper.entity.HelperActivityStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HelperStatusChangedEvent extends ApplicationEvent {
    private final Long helperId;
    private final HelperActivityStatus oldStatus;
    private final HelperActivityStatus newStatus;

    public HelperStatusChangedEvent(Long helperId, HelperActivityStatus oldStatus, HelperActivityStatus newStatus) {
        super(helperId);
        this.helperId = helperId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}