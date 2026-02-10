package com.save_help.Save_Help.nationalSubsidy.kafka;

public record RequirementsNationalSubsidy(String eventId,
                                          long userId,
                                          long occurredAtMillis,
                                          String reason) {
}
