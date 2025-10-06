package com.save_help.Save_Help.emergency.controller;

import com.save_help.Save_Help.emergency.dto.EmergencyRequestDto;
import com.save_help.Save_Help.emergency.dto.EmergencyResponseDto;
import com.save_help.Save_Help.emergency.service.EmergencyService;
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
    @PostMapping
    public ResponseEntity<EmergencyResponseDto> createEmergency(@RequestBody EmergencyRequestDto dto) {
        return ResponseEntity.ok(emergencyService.createEmergency(dto));
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<EmergencyResponseDto>> getAllEmergencies() {
        return ResponseEntity.ok(emergencyService.getAllEmergencies());
    }

    // 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyResponseDto> getEmergencyById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.getEmergencyById(id));
    }

    // 요청 취소
    @PutMapping("/{id}/cancel")
    public ResponseEntity<EmergencyResponseDto> cancelEmergency(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.cancelEmergency(id));
    }

    // 요청 해결
    @PutMapping("/{id}/resolve")
    public ResponseEntity<EmergencyResponseDto> resolveEmergency(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.resolveEmergency(id));
    }
}
