package com.save_help.Save_Help.helper.controller;

import com.save_help.Save_Help.helper.dto.*;
import com.save_help.Save_Help.helper.entity.*;
import com.save_help.Save_Help.helper.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/helpers")
@RequiredArgsConstructor
@Tag(name = "Helper API", description = "Helper(도움 제공자) 관리 API")
public class HelperController {

    private final HelperService helperService;
    private final HelperNotificationService notificationService;
    private final HelperAutoAssignmentService autoAssignmentService;
    private final HelperScheduleService scheduleService;
    private final HelperEmergencyContactService contactService;
    private final HelperAutoReassignService autoReassignService;
    private final HelperReviewService reviewService;
    private final HelperLocationService helperLocationService;

    // 생성
    @Operation(summary = "Helper 생성", description = "새로운 Helper를 등록합니다.")
    @PostMapping
    public ResponseEntity<HelperResponseDto> create(@RequestBody HelperRequestDto dto) {
        return ResponseEntity.ok(helperService.createHelper(dto));
    }

    // 단건 조회
    @Operation(summary = "Helper 단건 조회", description = "Helper ID로 특정 Helper를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<HelperResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(helperService.getHelper(id));
    }

    // 전체 조회
    @Operation(summary = "Helper 전체 조회", description = "Helper ID로 특정 Helper를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<HelperResponseDto>> getAll() {
        return ResponseEntity.ok(helperService.getAllHelpers());
    }

    // 수정
    @Operation(summary = "Helper 수정", description = "특정 Helper를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<HelperResponseDto> update(@PathVariable Long id,
                                                    @RequestBody HelperRequestDto dto) {
        return ResponseEntity.ok(helperService.updateHelper(id, dto));
    }

    // 삭제
    @Operation(summary = "Helper 삭제", description = "특정 Helper를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        helperService.deleteHelper(id);
        return ResponseEntity.noContent().build();
    }

    // 역할별 조회
    @Operation(summary = "Helper 역할별 조회", description = "특정 Helper를 삭제합니다.")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<HelperResponseDto>> getByRole(@PathVariable HelperRole role) {
        return ResponseEntity.ok(helperService.getHelpersByRole(role));
    }

    // 근무 가능자만 조회
    @Operation(summary = "근무 가능한 Helper 조회", description = "근무 가능한 Helper 조회")
    @GetMapping("/available")
    public ResponseEntity<List<HelperResponseDto>> getAvailableHelpers() {
        return ResponseEntity.ok(helperService.getAvailableHelpers());
    }

    // 역할별 + 근무 가능자 조회
    @Operation(summary = "근무 가능한 역할별  Helper 조회", description = "근무 가능한 역할별 Helper 조회")
    @GetMapping("/role/{role}/available")
    public ResponseEntity<List<HelperResponseDto>> getAvailableByRole(@PathVariable HelperRole role) {
        return ResponseEntity.ok(helperService.getAvailableHelpersByRole(role));
    }

    // 자동 배정 (내부적으로 Helper 자동 선택 후 배정)
    @PostMapping("/auto/{emergencyId}/{helperId}")
    public HelperAssignment autoAssign(
            @PathVariable Long emergencyId,
            @PathVariable Long helperId
    ) {
        return helperService.assignHelperAuto(emergencyId, helperId);
    }

    // 수동 배정
    @PostMapping("/manual/{emergencyId}/{helperId}")
    public HelperAssignment manualAssign(
            @PathVariable Long emergencyId,
            @PathVariable Long helperId
    ) {
        return helperService.assignHelperManual(emergencyId, helperId);
    }

    //Helper 긴급 연락 / 알림 API
    @Operation(summary = "Helper 긴급 연락", description = "특정 Helper에게 긴급 알림(문자/푸시)을 전송합니다.")
    @PostMapping("/notify")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequestDto dto) {
        notificationService.sendEmergencyNotification(dto);
        return ResponseEntity.ok("알림이 성공적으로 발송되었습니다.");
    }

    //긴급도 기반 자동 배정 API
    @Operation(summary = "긴급도 기반 자동 배정", description = "긴급상황의 긴급도를 기준으로 Helper를 자동 선택 후 배정합니다.")
    @PostMapping("/emergency/{emergencyId}")
    public ResponseEntity<HelperAssignmentResponseDto> autoAssignBySeverity(@PathVariable Long emergencyId) {
        return ResponseEntity.ok(autoAssignmentService.assignHelperBySeverity(emergencyId));
    }

    //Helper 근무 상태 관리 API
    @Operation(summary = "Helper 근무 상태 변경", description = "Helper의 근무 가능 여부(available)를 변경합니다.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<HelperResponseDto> updateHelperStatus(
            @PathVariable Long id,
            @RequestBody HelperStatusUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(helperService.updateAvailability(id, dto));
    }

    @Operation(summary = "Helper 출근", description = "Helper의 출근 시간을 기록합니다.")
    @PostMapping("/{id}/schedule/start")
    public ResponseEntity<HelperSchedule> clockIn(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.clockIn(id));
    }

    @Operation(summary = "Helper 퇴근", description = "Helper의 퇴근 시간을 기록합니다.")
    @PostMapping("/{id}/schedule/end")
    public ResponseEntity<HelperSchedule> clockOut(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.clockOut(id));
    }

    @Operation(summary = "Helper 근무 기록 조회", description = "Helper의 근무 기록 전체를 조회합니다.")
    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<HelperSchedule>> getSchedules(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getSchedules(id));
    }

    @Operation(summary = "Helper 총 근무시간 조회", description = "Helper의 총 근무시간(누적)을 조회합니다.")
    @GetMapping("/{id}/schedules/summary")
    public ResponseEntity<Map<String, Object>> getWorkSummary(@PathVariable Long id) {
        String summary = scheduleService.getTotalWorkSummary(id);
        return ResponseEntity.ok(Map.of("helperId", id, "totalWorkTime", summary));
    }

    @Operation(summary = "센터별 Helper 조회", description = "센터별 Helper를 조회합니다")
    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<Helper>> getHelpersByCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(helperService.getHelpersByCenter(centerId));
    }

    @Operation(summary = "병원별 Helper 조회", description = "병원별 Helper를 조회합니다")
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<Helper>> getHelpersByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(helperService.getHelpersByHospital(hospitalId));
    }

    @Operation(summary = "사용자(User)가 헬퍼(Helper)에게 연락 요청",
            description = "사용자가 특정 헬퍼에게 긴급 연락 메시지를 전송합니다. 주로 도움이 필요한 상황에서 사용됩니다.")
    @PostMapping("/user-to-helper")
    public ResponseEntity<HelperEmergencyContactDto> userToHelper(
            @RequestParam Long userId,
            @RequestParam Long helperId,
            @RequestParam String message) {
        HelperEmergencyContactDto contact = contactService.sendContact(userId, helperId, message, ContactType.USER_TO_HELPER);
        return ResponseEntity.ok(contact);
    }

    @Operation(
            summary = "헬퍼(Helper)가 사용자(User)에게 회신/연락",
            description = "헬퍼가 특정 사용자에게 응답 또는 연락 메시지를 전송합니다. 사용자 요청에 대한 응답 상황 등에 사용됩니다.")
    @PostMapping("/helper-to-user")
    public ResponseEntity<HelperEmergencyContactDto> helperToUser(
            @RequestParam Long helperId,
            @RequestParam Long userId,
            @RequestParam String message) {
        HelperEmergencyContactDto contact = contactService.sendContact(helperId, userId, message, ContactType.HELPER_TO_USER);
        return ResponseEntity.ok(contact);
    }

    @Operation(
            summary = "특정 헬퍼의 모든 연락 기록 조회",
            description = "특정 헬퍼 ID와 관련된 (받거나 보낸) 모든 긴급 연락 기록 리스트를 조회합니다.")
    @GetMapping("/helper/{id}")
    public ResponseEntity<List<HelperEmergencyContactDto>> getHelperContacts(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactsForHelper(id));
    }

    @Operation(
            summary = "특정 사용자의 모든 연락 기록 조회",
            description = "특정 사용자 ID와 관련된 (받거나 보낸) 모든 긴급 연락 기록 리스트를 조회합니다.")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<HelperEmergencyContactDto>> getUserContacts(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactsForUser(id));
    }

    @Operation(summary = "헬퍼 상태 업데이트", description = "근무 중, 이동 중, 응급 출동 중 등의 상태를 변경합니다.")
    @PatchMapping("/{id}/activity")
    public ResponseEntity<HelperResponseDto> updateActivityStatus(
            @PathVariable Long id,
            @RequestBody HelperActivityUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(helperService.updateActivityStatus(id, dto.getStatus()));
    }

    @Operation(summary = "헬퍼 자동 재배치", description = "헬퍼가 응답하지 않을 경우 다른 헬퍼로 자동 재배치합니다.")
    @PostMapping("/auto/reassign/{emergencyId}")
    public ResponseEntity<String> autoReassign(@PathVariable Long emergencyId) {
        autoReassignService.reassignIfUnresponsive(emergencyId);
        return ResponseEntity.ok("자동 재배치 작업이 실행되었습니다.");
    }

    //--------------------------------
    // Helper 평가 및 피드백
    //--------------------------------

    @Operation(summary = "헬퍼 평가 등록", description = "긴급 출동 완료 후 헬퍼에 대한 평가를 등록합니다.")
    @PostMapping("/helpers/reviews")
    public ResponseEntity<HelperReviewResponseDto> createReview(@RequestBody HelperReviewRequestDto dto) {
        return ResponseEntity.ok(reviewService.createReview(dto));
    }

    @Operation(summary = "헬퍼 평가 목록 조회", description = "특정 헬퍼의 평가 및 피드백 목록을 조회합니다.")
    @GetMapping("/helpers/reviews/{helperId}")
    public ResponseEntity<List<HelperReviewResponseDto>> getReviewsByHelper(@PathVariable Long helperId) {
        return ResponseEntity.ok(reviewService.getReviewsByHelper(helperId));
    }

    //--------------------------------
    // Helper 실시간 위치 업데이트 기능
    //--------------------------------
    @Operation(summary = "Helper 실시간 위치 갱신", description = "GPS 좌표를 갱신하고 Helper 정보를 반환합니다.")
    @PatchMapping("/location")
    public ResponseEntity<HelperLocationResponseDto> updateHelperLocation(
            @RequestBody HelperLocationUpdateRequestDto dto
    ) {
        HelperLocationResponseDto response = helperLocationService.updateHelperLocation(dto);
        return ResponseEntity.ok(response);
    }
}
