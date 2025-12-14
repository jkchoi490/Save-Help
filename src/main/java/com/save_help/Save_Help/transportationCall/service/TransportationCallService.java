package com.save_help.Save_Help.transportationCall.service;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperRole;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.transportationCall.dto.TransportationCallRequestDto;
import com.save_help.Save_Help.transportationCall.dto.TransportationCallResponseDto;
import com.save_help.Save_Help.transportationCall.entity.*;
import com.save_help.Save_Help.transportationCall.repository.TransportationCallRepository;
import com.save_help.Save_Help.transportationCall.repository.TransportationFeedbackRepository;
import com.save_help.Save_Help.transportationCall.repository.VehicleRepository;
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
    private final HelperRepository helperRepository;
    private final VehicleRepository vehicleRepository;
    private final TransportationFeedbackRepository feedbackRepository;


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

    @Transactional
    public Long requestCall(TransportationCall call) {
        // 요청 생성은 누구나 가능(유저)
        callRepository.save(call);
        return call.getId();
    }

    @Transactional
    public void assignDriverAndVehicle(Long callId, Long driverHelperId, Long vehicleId) {
        TransportationCall call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("이동 호출 정보를 찾을 수 없습니다."));

        if (call.getStatus() != TransportationCallStatus.REQUESTED) {
            throw new IllegalStateException("요청 상태의 호출만 배정할 수 있습니다.");
        }

        // 운전자 Helper인지
        Helper driver = helperRepository.findByIdAndRoleAndActiveTrue(driverHelperId, HelperRole.DRIVER)
                .orElseThrow(() -> new IllegalArgumentException("해당 헬퍼는 운전자 역할이 아니거나 비활성 상태입니다."));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("차량 정보를 찾을 수 없습니다."));

        // Vehicle에 이미 다른 운전자가 연결된 경우
        if (vehicle.getDriverName() != null && !vehicle.getDriverName().equals(driver.getName())) {
            throw new IllegalStateException("해당 차량은 다른 운전자에게 배치되어 있습니다.");
        }

        call.assign(driver, vehicle);
    }

    @Transactional
    public void dispatch(Long callId, Long driverHelperId) {
        TransportationCall call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("이동 호출 정보를 찾을 수 없습니다."));

        // 본인 배정 콜만 출발 가능
        if (call.getDriver() == null || !call.getDriver().getId().equals(driverHelperId)) {
            throw new IllegalStateException("배치된 운전자만 출발 처리를 할 수 있습니다.");
        }

        if (call.getStatus() != TransportationCallStatus.ASSIGNED) {
            throw new IllegalStateException("배치 완료 상태의 호출만 출발할 수 있습니다.");
        }

        call.dispatchNow();
    }

    @Transactional
    public void arrive(Long callId, Long driverHelperId) {
        TransportationCall call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("이동 호출 정보를 찾을 수 없습니다."));

        if (call.getDriver() == null || !call.getDriver().getId().equals(driverHelperId)) {
            throw new IllegalStateException("배치된 운전자만 도착 처리를 할 수 있습니다.");
        }

        if (call.getStatus() != TransportationCallStatus.DISPATCHED) {
            throw new IllegalStateException("출발 완료 상태의 호출만 도착 처리할 수 있습니다.");
        }

        call.arriveNow();
    }

    @Transactional
    public Long createFeedback(Long requesterUserId, Long callId, CreateTransportationFeedbackRequest req) {
        TransportationCall call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("이동 호출 정보를 찾을 수 없습니다."));

        //  요청자 본인 확인
        if (call.getRequester() == null || !call.getRequester().getId().equals(requesterUserId)) {
            throw new IllegalStateException("본인의 이동 호출에만 피드백을 남길 수 있습니다.");
        }

        // 도착 완료만 허용
        if (call.getStatus() != TransportationCallStatus.ARRIVED) {
            throw new IllegalStateException("도착 완료된 호출에만 피드백을 남길 수 있습니다.");
        }

        // 중복 작성 방지
        if (feedbackRepository.findByCallId(callId).isPresent()) {
            throw new IllegalStateException("이미 해당 호출에 대한 피드백이 작성되었습니다.");
        }

        //  평점 검증
        if (req.driverRating() == null && req.vehicleRating() == null) {
            throw new IllegalStateException("운전자 또는 차량 평점 중 하나는 입력해야 합니다.");
        }
        validateRating(req.driverRating(), "운전자 평점");
        validateRating(req.vehicleRating(), "차량 평점");

        User requester = userRepository.findById(requesterUserId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        var feedback = new TransportationFeedback(
                call,
                requester,
                call.getDriver(),
                call.getVehicle(),
                req.driverRating(),
                req.vehicleRating(),
                req.comment(),
                req.issue() == null ? TransportationFeedbackIssue.NONE : req.issue()
        );

        feedbackRepository.save(feedback);

        // (선택) 평균 평점 반영 등 집계 로직은 여기에서 처리 가능
        // updateDriverAverage(call.getDriver());
        // updateVehicleAverage(call.getVehicle());

        return feedback.getId();
    }

    //@Transactional(readOnly = true)
    @Transactional
    public TransportationFeedbackResponse getMyFeedback(Long requesterUserId, Long callId) {
        TransportationFeedback feedback = feedbackRepository.findByCallId(callId)
                .orElseThrow(() -> new IllegalArgumentException("피드백 정보를 찾을 수 없습니다."));

        if (!feedback.getRequester().getId().equals(requesterUserId)) {
            throw new IllegalStateException("본인의 피드백만 조회할 수 있습니다.");
        }

        return TransportationFeedbackResponse.from(feedback);
    }

    private void validateRating(Integer rating, String fieldName) {
        if (rating == null) return;
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(fieldName + "은(는) 1~5 사이여야 합니다.");
        }
    }

    // DTOs
    public record CreateTransportationFeedbackRequest(
            Integer driverRating,
            Integer vehicleRating,
            String comment,
            TransportationFeedbackIssue issue
    ) {}

    public record TransportationFeedbackResponse(
            Long id,
            Long callId,
            Integer driverRating,
            Integer vehicleRating,
            String comment,
            TransportationFeedbackIssue issue
    ) {
        public static TransportationFeedbackResponse from(TransportationFeedback f) {
            return new TransportationFeedbackResponse(
                    f.getId(),
                    f.getCall().getId(),
                    f.getDriverRating(),
                    f.getVehicleRating(),
                    f.getComment(),
                    f.getIssue()
            );
        }
    }

}
