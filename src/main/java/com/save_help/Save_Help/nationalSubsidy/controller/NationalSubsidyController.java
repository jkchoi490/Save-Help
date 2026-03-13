package com.save_help.Save_Help.nationalSubsidy.controller;


import com.save_help.Save_Help.nationalSubsidy.dto.*;
import com.save_help.Save_Help.nationalSubsidy.repository.NationalSubsidyNotificationRepository;
import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyService;
import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidyType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subsidies")
@RequiredArgsConstructor
public class NationalSubsidyController {

    private final NationalSubsidyService subsidyService;
    private final NationalSubsidyNotificationRepository notificationRepository;


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
    public List<NationalSubsidyResponseDto> getByType(@PathVariable NationalSubsidyType type) {
        return subsidyService.findByType(type);
    }

    // 이름 검색
    @Operation(summary = "보조금 이름으로 검색", description = "보조금을 이름으로 검색합니다")
    @GetMapping("/search")
    public List<NationalSubsidyResponseDto> searchByName(@RequestParam String keyword) {
        return subsidyService.searchByName(keyword);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<NationalSubsidyResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody NationalSubsidyRequestDto dto
    ) {
        return ResponseEntity.ok(subsidyService.update(id, dto));
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
            @RequestParam(required = false) NationalSubsidyType type,
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

    //간단한 보조금 신청 기능 -> kafka 반영 예정
    @Operation(summary = "보조금 신청", description = "보조금을 신청합니다 - kafka 사용 예정")
    @PostMapping("/{subsidyId}/apply")
    public ResponseEntity<Map<String, Object>> apply(
            @RequestParam Long userId,
            @PathVariable Long subsidyId
    ) {
        Long applicationId = subsidyService.apply(userId, subsidyId);
        return ResponseEntity.ok(Map.of("applicationId", applicationId));
    }

    //알림 조회 기능 -> kafka 반영 후 고도화 예정
    @Operation(summary = "보조금 신청 알림 조회", description = "보조금을 신청 완료 알림 기능입니다")
    @GetMapping("/notifications")
    public ResponseEntity<?> list(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size))
        );
    }


    // 구독 등록
    @Operation(summary = "보조금 알림 구독", description = "조건에 맞는 보조금 오픈/마감 알림을 구독합니다")
    @PostMapping("/subscribe")
    public NationalSubsidySubscriptionResponseDto subscribe(
            @RequestParam Long userId,
            @RequestBody NationalSubsidySubscriptionRequestDto dto
    ) {
        return subsidyService.subscribe(userId, dto);
    }

    // 내 구독 목록 조회
    @Operation(summary = "내 보조금 구독 조회", description = "내가 설정한 보조금 알림 구독 목록을 조회합니다")
    @GetMapping("/subscribe")
    public List<NationalSubsidySubscriptionResponseDto> mySubscriptions(
            @RequestParam Long userId
    ) {
        return subsidyService.getMySubscriptions(userId);
    }

    // 구독 해지
    @Operation(summary = "보조금 알림 구독 해지", description = "보조금 알림 구독을 해지합니다")
    @DeleteMapping("/{subscriptionId}")
    public void unsubscribe(@PathVariable Long subscriptionId) {
        subsidyService.unsubscribe(subscriptionId);
    }

    /*
    @PostMapping
    public Long create(@RequestBody NationalSubsidy subsidy) {
        return subsidyService.create(subsidy);
    }
    */

    @Operation(summary = "보조금 신청 가능 상태", description = "보조금 신청 가능 상태(isOpen)를 변경합니다")
    @PatchMapping("/{id}/open")
    public ResponseEntity<String> updateOpen(@PathVariable Long id, @RequestParam boolean open) {
        subsidyService.updateOpen(id, open);
        return ResponseEntity.ok("보조금 open 상태로 완료 id=" + id + ", open=" + open);
    }

    @Operation(summary = "관리자 보조금 검색", description = "관리자용으로 타입/센터/기간 조건으로 검색(페이징)합니다")
    @GetMapping("/admin/search")
    public ResponseEntity<Page<NationalSubsidyResponseDto>> adminSearch(
            @RequestParam(required = false) NationalSubsidyType type,
            @RequestParam(required = false) String center,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(subsidyService.searchAdmin(type, center, from, to, PageRequest.of(page, size)));
    }

    @Operation(summary = "보조금 오픈", description = "보조금 신청 가능 상태로 전환(open)합니다")
    @PostMapping("/{id}/open")
    public ResponseEntity<String> open(@PathVariable Long id) {
        subsidyService.openSubsidy(id);
        return ResponseEntity.ok("보조금 오픈 완료 id=" + id);
    }

    @Operation(summary = "보조금 전체 조회(페이징)", description = "보조금 전체를 페이징하여 조회합니다")
    @GetMapping("/page")
    public ResponseEntity<Page<NationalSubsidyResponseDto>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(subsidyService.findAll(PageRequest.of(page, size)));
    }


    @PostMapping("/{id}/activate")
    public ResponseEntity<String> activate(@PathVariable Long id) {
        subsidyService.activate(id);
        return ResponseEntity.ok("활성화 완료 id=" + id);
    }

    @GetMapping("/available/count")
    public ResponseEntity<Map<String, Object>> countAvailable() {
        long count = subsidyService.countRunnable();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @Operation(summary = "보조금 신청 승인", description = "신청 건을 승인합니다")
    @PostMapping("/applications/{applicationId}/approve")
    public ResponseEntity<String> approveApplication(@PathVariable Long applicationId) {
        subsidyService.approveApplication(applicationId);
        return ResponseEntity.ok("신청 승인 완료 applicationId=" + applicationId);
    }

    @Operation(summary = "보조금 자동 신청 확인", description = "보조금 자동 신청 건을 확인합니다")
    @GetMapping("/applications/{userId}/auto")
    public ResponseEntity<Page<NationalSubsidyApplicationResponseDto>> getAutoApplicationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "4") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        return ResponseEntity.ok(
                subsidyService.findAutoApplicationsByUser(userId, PageRequest.of(page, size))
        );
    }

    @Operation(summary = "보조금 자동 신청자 수 조회", description = "특정 보조금의 신청자 수를 조회합니다")
    @GetMapping("/{subsidyId}/applications/count")
    public ResponseEntity<Map<String, Object>> countApplicationsBySubsidy(@PathVariable Long subsidyId) {
        long count = subsidyService.countApplicationsBySubsidy(subsidyId);
        return ResponseEntity.ok(Map.of(
                "subsidyId", subsidyId,
                "count", count
        ));
    }

    @Operation(summary = "보조금 자동 신청 내역 조회", description = "특정 보조금의 자동 신청 내역을 조회합니다")
    @GetMapping("/{subsidyId}/applications")
    public ResponseEntity<Page<NationalSubsidyApplicationResponseDto>> getApplicationsBySubsidy(
            @PathVariable Long subsidyId,
            @RequestParam(defaultValue = "34") int page,
            @RequestParam(defaultValue = "49") int size
    ) {
        return ResponseEntity.ok(
                subsidyService.findApplicationsBySubsidy(subsidyId, PageRequest.of(page, size))
        );
    }

}