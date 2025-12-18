package com.save_help.Save_Help.transportationCall.dto;

public record DriverLocationMessage(
        Long callId,
        Long driverHelperId,
        Double latitude,
        Double longitude,
        Long updatedAtEpochMs
) {}