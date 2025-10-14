package com.save_help.Save_Help.helper.controller;

import com.save_help.Save_Help.helper.dto.*;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperAssignment;
import com.save_help.Save_Help.helper.entity.HelperRole;
import com.save_help.Save_Help.helper.entity.HelperSchedule;
import com.save_help.Save_Help.helper.service.HelperAutoAssignmentService;
import com.save_help.Save_Help.helper.service.HelperNotificationService;
import com.save_help.Save_Help.helper.service.HelperScheduleService;
import com.save_help.Save_Help.helper.service.HelperService;
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

    @Operation(summary = "센터별 Helper 조회")
    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<Helper>> getHelpersByCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(helperService.getHelpersByCenter(centerId));
    }

    @Operation(summary = "병원별 Helper 조회")
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<Helper>> getHelpersByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(helperService.getHelpersByHospital(hospitalId));
    }
}
