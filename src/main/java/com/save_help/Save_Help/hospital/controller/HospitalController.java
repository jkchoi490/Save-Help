package com.save_help.Save_Help.hospital.controller;

import com.save_help.Save_Help.hospital.dto.HospitalRequestDto;
import com.save_help.Save_Help.hospital.dto.HospitalResponseDto;
import com.save_help.Save_Help.hospital.entity.Hospital;
import com.save_help.Save_Help.hospital.entity.HospitalType;
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

    // 1. 사용자 위치 기준 가장 가까운 병원 조회
    // GET /api/hospitals/nearest?lat=37.5&lng=127.0&type=HOSPITAL
    @GetMapping("/nearest")
    public ResponseEntity<Hospital> getNearestHospital(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam HospitalType type) {

        Hospital nearest = hospitalService.findNearestHospital(lat, lng, type);
        return ResponseEntity.ok(nearest);
    }

    //남은 병상 있는 병원 + 사용자 위치 기준 가까운 병원 조회
    //GET /api/hospitals/nearest-with-bed?lat=37.5&lng=127.0&type=HOSPITAL
    @GetMapping("/nearest-with-bed")
    public ResponseEntity<Hospital> getNearestHospitalWithAvailableBed(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam HospitalType type) {

        Hospital nearestWithBed = hospitalService.findNearestHospitalWithAvailableBed(lat, lng, type);
        return ResponseEntity.ok(nearestWithBed);
    }

    // 병상 수 설정
    @PostMapping("/{id}/beds")
    public ResponseEntity<Void> setBedCount(@PathVariable Long id, @RequestParam int bedCount) {
        hospitalService.setBedCount(id, bedCount);
        return ResponseEntity.ok().build();
    }

    // 병상 감소
    @PostMapping("/{id}/beds/decrease")
    public ResponseEntity<Void> decreaseBedCount(@PathVariable Long id) {
        hospitalService.decreaseBedCount(id);
        return ResponseEntity.ok().build();
    }

    // 병상 증가
    @PostMapping("/{id}/beds/increase")
    public ResponseEntity<Void> increaseBedCount(@PathVariable Long id) {
        hospitalService.increaseBedCount(id);
        return ResponseEntity.ok().build();
    }

}
