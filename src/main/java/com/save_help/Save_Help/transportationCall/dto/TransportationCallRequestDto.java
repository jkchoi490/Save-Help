package com.save_help.Save_Help.transportationCall.dto;

import com.save_help.Save_Help.transportationCall.entity.TransportationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationCallRequestDto {
    private Long userId;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private TransportationType type;
}
