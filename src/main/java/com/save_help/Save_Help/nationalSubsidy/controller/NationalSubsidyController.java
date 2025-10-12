package com.save_help.Save_Help.nationalSubsidy.controller;


import com.save_help.Save_Help.nationalSubsidy.dto.NationalSubsidyResponseDto;
import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyService;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import com.save_help.Save_Help.nationalSubsidy.dto.NationalSubsidyRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subsidies")
@RequiredArgsConstructor
public class NationalSubsidyController {

    private final NationalSubsidyService subsidyService;

    // 보조금 등록
    @Operation(summary = "보조금 등록", description = "보조금을 등록합니다")
    @PostMapping
    public NationalSubsidyResponseDto create(@RequestBody NationalSubsidyRequestDto dto) {
        return subsidyService.create(dto);
    }

    // 전체 조회
    @Operation(summary = "보조금 전체 조회", description = "보조금 전체를 조회합니다")
    @GetMapping
    public List<NationalSubsidyResponseDto> getAll() {
        return subsidyService.findAll();
    }

    // 단일 조회
    @Operation(summary = "보조금 단일 조회", description = "보조금을 단일 조회합니다")
    @GetMapping("/{id}")
    public NationalSubsidyResponseDto getById(@PathVariable Long id) {
        return subsidyService.findById(id);
    }

    // 유형별 조회
    @Operation(summary = "보조금 유형별 조회", description = "보조금을 유형별로 조회합니다")
    @GetMapping("/type/{type}")
    public List<NationalSubsidyResponseDto> getByType(@PathVariable SubsidyType type) {
        return subsidyService.findByType(type);
    }

    // 이름 검색
    @Operation(summary = "보조금 이름으로 검색", description = "보조금을 이름으로 검색합니다")
    @GetMapping("/search")
    public List<NationalSubsidyResponseDto> searchByName(@RequestParam String keyword) {
        return subsidyService.searchByName(keyword);
    }

    // 수정
    @Operation(summary = "보조금 수정", description = "보조금을 수정합니다")
    @PutMapping("/{id}")
    public NationalSubsidyResponseDto update(@PathVariable Long id, @RequestBody NationalSubsidyRequestDto dto) {
        return subsidyService.update(id, dto);
    }

    // 삭제
    @Operation(summary = "보조금 삭제", description = "보조금을 삭제합니다")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        subsidyService.delete(id);
    }

    // 특정 유저만 자동 신청
    @Operation(summary = "특정 유저에 대해 자동 신청", description = "특정 유저에 대해 보조금을 자동신청합니다")
    @PostMapping("/auto-apply/{userId}")
    public ResponseEntity<String> autoApply(@PathVariable Long userId) {
        subsidyService.autoApplyForUser(userId);
        return ResponseEntity.ok("자동 신청 완료 for userId=" + userId);
    }

    // 모든 유저에 대해 자동 신청
    @Operation(summary = "모든 유저에 대해 자동 신청", description = "모든 유저에 대해 보조금을 자동신청합니다")
    @PostMapping("/auto-apply/all")
    public ResponseEntity<String> autoApplyAll() {
        subsidyService.autoApplyForAllUsers();
        return ResponseEntity.ok("전체 유저 자동 신청 완료");
    }

    //맞춤형 보조금 추천
    @Operation(summary = "맞춤형 보조금 추천", description = "사용자 조건에 맞는 보조금을 추천합니다")
    @GetMapping("/recommend")
    public List<NationalSubsidyResponseDto> recommend(
            @RequestParam Integer age,
            @RequestParam(required = false) String incomeLevel,
            @RequestParam(required = false) Boolean disability,
            @RequestParam(required = false) Boolean emergency) {
        return subsidyService.recommend(age, incomeLevel, disability, emergency);
    }

    //신청 가능한 보조금 조회
    @Operation(summary = "신청 가능 보조금 조회", description = "현재 신청 가능한 보조금만 조회합니다")
    @GetMapping("/available")
    public List<NationalSubsidyResponseDto> getAvailableSubsidies() {
        return subsidyService.findAvailableSubsidies();
    }

    //다양한 조건으로 보조금 검색
    @Operation(summary = "보조금 세부 검색", description = "다양한 조건으로 보조금을 검색합니다")
    @GetMapping("/filter")
    public List<NationalSubsidyResponseDto> filter(
            @RequestParam(required = false) SubsidyType type,
            @RequestParam(required = false) String incomeLevel,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Boolean disabilityRequired) {
        return subsidyService.filter(type, incomeLevel, minAge, maxAge, disabilityRequired);
    }

    //유저의 보조금 신청 내역 조회
    @Operation(summary = "유저의 신청 내역 조회", description = "특정 유저가 신청한 보조금 내역을 조회합니다")
    @GetMapping("/applications/{userId}")
    public List<NationalSubsidyResponseDto> getApplicationsByUser(@PathVariable Long userId) {
        return subsidyService.findApplicationsByUser(userId);
    }

    //보조금 통계 조회
    @Operation(summary = "보조금 통계 조회", description = "센터별 또는 유형별 통계를 조회합니다")
    @GetMapping("/stats")
    public Map<String, Object> getStatistics() {
        return subsidyService.getStatistics();
    }

    //보조금 상태 변경
    @Operation(summary = "보조금 상태 변경", description = "보조금의 활성/비활성 상태를 변경합니다")
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        subsidyService.updateStatus(id, active);
        return ResponseEntity.ok("보조금 상태 변경 완료");
    }

    //보조금 데이터 다운로드
    @Operation(summary = "보조금 데이터 다운로드", description = "CSV 파일로 보조금 목록을 다운로드합니다")
    @GetMapping("/export")
    public ResponseEntity<Resource> exportToCsv() {
        Resource csv = subsidyService.exportToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=보조금목록.csv")
                .body(csv);
    }



}