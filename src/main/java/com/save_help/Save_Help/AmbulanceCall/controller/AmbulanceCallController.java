package com.save_help.Save_Help.AmbulanceCall.controller;

import com.save_help.Save_Help.AmbulanceCall.dto.AmbulanceCallDto;
import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCall;
import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCallStatus;
import com.save_help.Save_Help.AmbulanceCall.service.AmbulanceCallService;
import com.save_help.Save_Help.emergency.Emergency;
import com.save_help.Save_Help.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance-calls")
@Tag(name = "AmbulanceCall", description = "응급차 호출 관리 API")
@RequiredArgsConstructor
public class AmbulanceCallController {

    private final AmbulanceCallService service;
    private final EntityManager em; // 단순 연관 조회용 (실제 구현 시 Service로 대체 가능)

    @Operation(summary = "응급차 호출 생성", description = "응급차 호출을 생성합니다")
    @PostMapping
    public ResponseEntity<AmbulanceCall> create(@RequestBody AmbulanceCallDto dto) {
        User requester = em.find(User.class, dto.getRequesterId());
        Emergency emergency = em.find(Emergency.class, dto.getEmergencyId());
        return ResponseEntity.ok(service.create(dto, requester, emergency));
    }

    @Operation(summary = "모든 호출 조회", description = "모든 호출을 조회합니다")
    @GetMapping
    public ResponseEntity<List<AmbulanceCall>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "ID로 호출 조회", description = "ID로 호출을 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<AmbulanceCall> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "상태별 호출 조회", description = "상태별 호출을 조회합니다")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AmbulanceCall>> getByStatus(@PathVariable AmbulanceCallStatus status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    @Operation(summary = "요청자별 호출 조회", description = "요청자별 호출을 조회합니다")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AmbulanceCall>> getByRequester(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByRequester(userId));
    }

    @Operation(summary = "긴급 상황별 호출 조회", description = "긴급상황별 호출을 조회합니다")
    @GetMapping("/emergency/{emergencyId}")
    public ResponseEntity<List<AmbulanceCall>> getByEmergency(@PathVariable Long emergencyId) {
        return ResponseEntity.ok(service.getByEmergency(emergencyId));
    }

    @Operation(summary = "호출 상태 업데이트", description = "호출 상태를 업데이트 합니다")
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<AmbulanceCall> updateStatus(@PathVariable Long id, @PathVariable AmbulanceCallStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @Operation(summary = "호출 삭제", description = "호출을 삭제합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
