package com.save_help.Save_Help.emergency.controller;

import com.save_help.Save_Help.emergency.dto.EmergencyRequestDto;
import com.save_help.Save_Help.emergency.dto.EmergencyResponseDto;
import com.save_help.Save_Help.emergency.service.EmergencyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergencies")
@RequiredArgsConstructor
public class EmergencyController {

    private final EmergencyService emergencyService;

    // 긴급 요청 등록
    @Operation(summary = "긴급 요청 등록", description = "긴급 요청을 등록합니다")
    @PostMapping
    public ResponseEntity<EmergencyResponseDto> createEmergency(@RequestBody EmergencyRequestDto dto) {
        return ResponseEntity.ok(emergencyService.createEmergency(dto));
    }

    // 전체 조회
    @Operation(summary = "긴급 요청 전체 조회", description = "긴급 요청 전체 조회합니다")
    @GetMapping
    public ResponseEntity<List<EmergencyResponseDto>> getAllEmergencies() {
        return ResponseEntity.ok(emergencyService.getAllEmergencies());
    }

    // 단일 조회
    @Operation(summary = "긴급 요청 단일 조회", description = "긴급 요청을 단일 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyResponseDto> getEmergencyById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.getEmergencyById(id));
    }

    // 요청 취소
    @Operation(summary = "긴급 요청 취소", description = "긴급 요청을 취소합니다")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<EmergencyResponseDto> cancelEmergency(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.cancelEmergency(id));
    }

    // 요청 해결
    @Operation(summary = "긴급 요청을 해결", description = "긴급 요청을 해결 상태로 바꿉니다")
    @PutMapping("/{id}/resolve")
    public ResponseEntity<EmergencyResponseDto> resolveEmergency(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.resolveEmergency(id));
    }
}
