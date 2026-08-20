package com.save_help.Save_Help.nationalSubsidy.kafka.event;

public record NationalSubsidySavedEvent(Long subsidyId,
                                        String eventId) {
}
