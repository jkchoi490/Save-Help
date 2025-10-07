package com.save_help.Save_Help.hospital.controller;

import com.save_help.Save_Help.hospital.dto.HospitalRequestDto;
import com.save_help.Save_Help.hospital.dto.HospitalResponseDto;
import com.save_help.Save_Help.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    // 병원 등록
    @PostMapping
    public ResponseEntity<HospitalResponseDto> createHospital(@RequestBody HospitalRequestDto dto) {
        return ResponseEntity.ok(hospitalService.createHospital(dto));
    }

    // 병원 조회 (단일)
    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponseDto> getHospital(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.getHospital(id));
    }

    // 병원 전체 조회
    @GetMapping
    public ResponseEntity<List<HospitalResponseDto>> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }

    // 병원 수정
    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponseDto> updateHospital(@PathVariable Long id, @RequestBody HospitalRequestDto dto) {
        return ResponseEntity.ok(hospitalService.updateHospital(id, dto));
    }

    // 병원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        hospitalService.deleteHospital(id);
        return ResponseEntity.noContent().build();
    }

    // 남은 병상 수 조회
    @GetMapping("/{id}/remaining-beds")
    public ResponseEntity<Integer> getRemainingBeds(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.getRemainingBeds(id));
    }
}
