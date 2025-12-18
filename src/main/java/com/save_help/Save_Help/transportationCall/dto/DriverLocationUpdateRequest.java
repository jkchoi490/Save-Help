package com.save_help.Save_Help.transportationCall.dto;

import jakarta.validation.constraints.NotNull;

public record DriverLocationUpdateRequest(
        @NotNull Double latitude,
        @NotNull Double longitude,
        Long sentAtEpochMs
) {}