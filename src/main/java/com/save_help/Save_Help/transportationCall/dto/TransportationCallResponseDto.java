package com.save_help.Save_Help.transportationCall.dto;

import com.save_help.Save_Help.transportationCall.entity.TransportationCallStatus;
import com.save_help.Save_Help.transportationCall.entity.TransportationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransportationCallResponseDto {
    private Long id;
    private Long requesterId;
    private Long vehicleId;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private LocalDateTime requestedAt;
    private TransportationCallStatus status;
    private TransportationType type;
}
