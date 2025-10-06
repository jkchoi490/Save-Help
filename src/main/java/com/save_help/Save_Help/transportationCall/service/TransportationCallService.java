package com.save_help.Save_Help.transportationCall.service;

import com.save_help.Save_Help.transportationCall.dto.TransportationCallRequestDto;
import com.save_help.Save_Help.transportationCall.dto.TransportationCallResponseDto;
import com.save_help.Save_Help.transportationCall.entity.*;
import com.save_help.Save_Help.transportationCall.repository.TransportationCallRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportationCallService {

    private final TransportationCallRepository callRepository;
    private final UserRepository userRepository;

    @Transactional
    public TransportationCallResponseDto createCall(TransportationCallRequestDto dto) {
        User requester = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TransportationCall call = new TransportationCall(
                requester,
                dto.getPickupLatitude(),
                dto.getPickupLongitude(),
                dto.getDropoffLatitude(),
                dto.getDropoffLongitude(),
                dto.getType()
        );

        callRepository.save(call);

        return toDto(call);
    }

    public List<TransportationCallResponseDto> getCallsByUser(Long userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return callRepository.findByRequester(requester)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransportationCallResponseDto updateStatus(Long callId, TransportationCallStatus status) {
        TransportationCall call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        call.setStatus(status);
        if (status == TransportationCallStatus.DISPATCHED) {
            call.setDispatchedAt(java.time.LocalDateTime.now());
        } else if (status == TransportationCallStatus.ARRIVED) {
            call.setArrivedAt(java.time.LocalDateTime.now());
        }

        return toDto(call);
    }

    private TransportationCallResponseDto toDto(TransportationCall call) {
        return TransportationCallResponseDto.builder()
                .id(call.getId())
                .requesterId(call.getRequester().getId())
                .vehicleId(call.getVehicle() != null ? call.getVehicle().getId() : null)
                .pickupLatitude(call.getPickupLatitude())
                .pickupLongitude(call.getPickupLongitude())
                .dropoffLatitude(call.getDropoffLatitude())
                .dropoffLongitude(call.getDropoffLongitude())
                .requestedAt(call.getRequestedAt())
                .status(call.getStatus())
                .type(call.getType())
                .build();
    }
}
