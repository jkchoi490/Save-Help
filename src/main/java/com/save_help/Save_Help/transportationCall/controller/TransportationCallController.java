package com.save_help.Save_Help.transportationCall.controller;

import com.save_help.Save_Help.transportationCall.dto.DriverLocationMessage;
import com.save_help.Save_Help.transportationCall.dto.DriverLocationUpdateRequest;
import com.save_help.Save_Help.transportationCall.dto.TransportationCallRequestDto;
import com.save_help.Save_Help.transportationCall.dto.TransportationCallResponseDto;
import com.save_help.Save_Help.transportationCall.entity.TransportationCallStatus;
import com.save_help.Save_Help.transportationCall.service.TransportationCallLocationService;
import com.save_help.Save_Help.transportationCall.service.TransportationCallService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transportation-calls")
@RequiredArgsConstructor
public class TransportationCallController {

    private final TransportationCallService callService;
    private final TransportationCallLocationService locationService;
    private final SimpMessagingTemplate messagingTemplate;

    //교통 호출 생성 기능 개발
    @Operation(summary = "교통 호출 생성", description = "교통 호출을 생성합니다")
    @PostMapping
    public TransportationCallResponseDto createCall(@RequestBody TransportationCallRequestDto dto) {
        return callService.createCall(dto);
    }

    //특정 사용자의 모든 호출 내역 조회 기능
    @Operation(summary = "특정 사용자의 모든 호출 내역 조회", description = "특정 사용자의 모든 호출 내역을 조회합니다")
    @GetMapping("/user/{userId}")
    public List<TransportationCallResponseDto> getCallsByUser(@PathVariable Long userId) {
        return callService.getCallsByUser(userId);
    }

    //호출 상태 변경
    @Operation(summary = "호출 상태 변경", description = "호출 상태를 변경합니다")
    @PatchMapping("/{callId}/status")
    public TransportationCallResponseDto updateStatus(
            @PathVariable Long callId,
            @RequestParam TransportationCallStatus status
    ) {
        return callService.updateStatus(callId, status);
    }

    @Operation(summary = "운전자/차량 배치", description = "이동 호출에 운전자(Helper DRIVER)와 차량을 배치합니다")
    @PostMapping("/{callId}/assign")
    public void assignDriverAndVehicle(
            @PathVariable Long callId,
            @RequestBody @Valid TransportationAssignRequest req
    ) {
        callService.assignDriverAndVehicle(callId, req.driverHelperId(), req.vehicleId());
    }

    public record TransportationAssignRequest(
            @NotNull Long driverHelperId,
            @NotNull Long vehicleId
    ) {}

    @Operation(summary = "출발 처리", description = "배치된 운전자가 출발 처리합니다")
    @PostMapping("/{callId}/dispatch")
    public void dispatch(
            @PathVariable Long callId,
            @RequestParam @NotNull Long driverHelperId
    ) {
        callService.dispatch(callId, driverHelperId);
    }

    @Operation(summary = "도착 처리", description = "배치된 운전자가 도착 처리합니다")
    @PostMapping("/{callId}/arrive")
    public void arrive(
            @PathVariable Long callId,
            @RequestParam @NotNull Long driverHelperId
    ) {
        callService.arrive(callId, driverHelperId);
    }

    @Operation(summary = "이동 피드백 작성", description = "도착 완료된 이동 호출에 대해 운전자/차량 피드백을 작성합니다")
    @PostMapping("/{callId}/feedback")
    public Long createFeedback(
            @PathVariable Long callId,
            @RequestParam Long requesterUserId,
            @RequestBody @Valid TransportationCallService.CreateTransportationFeedbackRequest req
    ) {
        return callService.createFeedback(requesterUserId, callId, req);
    }

    @Operation(summary = "내 이동 피드백 조회", description = "내가 작성한 이동 호출 피드백을 조회합니다")
    @GetMapping("/{callId}/feedback")
    public TransportationCallService.TransportationFeedbackResponse getMyFeedback(
            @PathVariable Long callId,
            @RequestParam Long requesterUserId
    ) {
        return callService.getMyFeedback(requesterUserId, callId);
    }

    /**
     * 운전자 앱이 send:
     *   /app/transportation-calls/{callId}/location
     *
     * 요청자 화면이 subscribe:
     *   /topic/transportation-calls/{callId}/location
     */
    @MessageMapping("/{callId}/location")
    public void updateLocation(
            @DestinationVariable Long callId,
            @Valid DriverLocationUpdateRequest req
    ) {
        DriverLocationMessage msg = locationService.updateAndCache(callId, req);

        messagingTemplate.convertAndSend(
                "/topic/transportation-calls/" + callId + "/location",
                msg
        );
    }

    @Operation(summary = "운전자 마지막 위치 조회", description = "재접속/새로고침 시 Redis에 저장된 마지막 위치를 조회합니다.")
    @GetMapping("/{callId}/last-location")
    public DriverLocationMessage lastLocation(@PathVariable Long callId) {
        return locationService.getLastLocation(callId);
    }
}
