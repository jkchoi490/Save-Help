package com.save_help.Save_Help.dailyNecessities.controller;

import com.save_help.Save_Help.dailyNecessities.dto.*;
import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/necessities")
public class DailyNecessitiesController {

    private final DailyNecessitiesService necessitiesService;
    private final UserNecessityRequestService requestService;
    private final DailyNecessitiesStockService stockService;
    private final DailyNecessitiesStatisticsService statisticsService;
    private final DailyNecessitiesReportService reportService;
    private final DailyNecessitiesDonationService donationService;

    public DailyNecessitiesController(
            DailyNecessitiesService necessitiesService,
            UserNecessityRequestService requestService,
            DailyNecessitiesStockService stockService,
            DailyNecessitiesStatisticsService statisticsService,
            DailyNecessitiesReportService reportService,
            DailyNecessitiesDonationService donationService) {
        this.necessitiesService = necessitiesService;
        this.requestService = requestService;
        this.stockService = stockService;
        this.statisticsService = statisticsService;
        this.reportService = reportService;
        this.donationService = donationService;
    }

    // ------------------------------------
    // 기본 CRUD
    // ------------------------------------

    @GetMapping("/getAll")
    public List<DailyNecessitiesDto> getAll() {
        return necessitiesService.getAll();
    }

    @GetMapping("/{id}")
    public DailyNecessitiesDto getById(@PathVariable Long id) {
        return necessitiesService.getById(id);
    }

    @GetMapping("/category/{category}")
    public List<DailyNecessitiesDto> getByCategory(@PathVariable NecessityCategory category) {
        return necessitiesService.getByCategory(category);
    }

    @GetMapping("/search")
    public List<DailyNecessitiesDto> searchByName(@RequestParam String name) {
        return necessitiesService.searchByName(name);
    }

    @PostMapping("/create")
    public DailyNecessitiesDto create(@RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.create(dto);
    }

    @PutMapping("/{id}")
    public DailyNecessitiesDto update(@PathVariable Long id, @RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        necessitiesService.delete(id);
    }

    // ------------------------------------
    // 재고 관리
    // ------------------------------------

    @PostMapping("/{itemId}/reduce")
    public ResponseEntity<DailyNecessities> reduceStock(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(stockService.reduceStock(itemId, userId, quantity));
    }

    @PostMapping("/{itemId}/add/user")
    public ResponseEntity<DailyNecessities> addStockByUser(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(stockService.addStock(itemId, userId, quantity));
    }

    @PostMapping("/{itemId}/add/admin")
    public ResponseEntity<DailyNecessities> addStockByAdmin(
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(necessitiesService.addStock(itemId, quantity));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<DailyNecessities>> getLowStockItems(
            @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(necessitiesService.getLowStockItems(threshold));
    }

    @GetMapping("/history/item/{itemId}")
    public ResponseEntity<List<DailyNecessitiesHistory>> getHistoryByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(stockService.getHistoryByItem(itemId));
    }

    @GetMapping("/period")
    public ResponseEntity<List<DailyNecessitiesHistory>> getHistoryByPeriod(
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(stockService.getHistoryByPeriod(
                LocalDateTime.parse(start), LocalDateTime.parse(end)
        ));
    }

    // ------------------------------------
    // 통계
    // ------------------------------------

    @GetMapping("/center")
    public ResponseEntity<List<StockStatisticsDto>> getStockByCenter() {
        return ResponseEntity.ok(statisticsService.getStockByCenter());
    }

    // ------------------------------------
    // 센터별 품목 조회
    // ------------------------------------

    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<DailyNecessitiesDto>> getByCenter(@PathVariable Long centerId) {
        List<DailyNecessitiesDto> list = necessitiesService.getByCenter(centerId);
        return ResponseEntity.ok(list);
    }

    // ------------------------------------
    // 사용자 자동 신청
    // ------------------------------------

    @PostMapping("/request")
    public ResponseEntity<UserNecessityRequestResponseDto> createRequest(
            @RequestBody UserNecessityRequestDto requestDto) {
        UserNecessityRequest request = requestService.createRequest(
                requestDto.getUserId(),
                requestDto.getItemId(),
                requestDto.getQuantity()
        );
        return ResponseEntity.ok(UserNecessityRequestResponseDto.fromEntity(request));
    }

    @GetMapping("/request/user/{userId}")
    public ResponseEntity<List<UserNecessityRequestResponseDto>> getRequestsByUser(@PathVariable Long userId) {
        List<UserNecessityRequestResponseDto> responses = requestService.getRequestsByUser(userId)
                .stream()
                .map(UserNecessityRequestResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/request/{requestId}")
    public ResponseEntity<String> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestParam UserNecessityRequest.RequestStatus status) {
        requestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok("신청 상태가 변경되었습니다.");
    }

    // ------------------------------------
    // 관리자용 승인 / 거절
    // ------------------------------------

    @GetMapping("/admin/pending-items")
    public ResponseEntity<List<DailyNecessities>> getPendingItems() {
        List<DailyNecessities> list = necessitiesService.getAllForAdmin().stream()
                .filter(item -> item.getApprovalStatus() == DailyNecessities.ApprovalStatus.PENDING)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/admin/{id}/approve")
    public ResponseEntity<String> approveItem(@PathVariable Long id) {
        necessitiesService.approveItem(id);
        return ResponseEntity.ok("품목 승인 완료");
    }

    @PatchMapping("/admin/{id}/reject")
    public ResponseEntity<String> rejectItem(@PathVariable Long id) {
        necessitiesService.rejectItem(id);
        return ResponseEntity.ok("품목 거부 완료");
    }

    // ------------------------------------
    // 사용자 기부 기능
    // ------------------------------------

    // 1. 기부 요청
    @PostMapping("/donation")
    public DailyNecessitiesDonationResponseDto donate(@RequestBody DailyNecessitiesDonationRequestDto requestDto) {
        DailyNecessitiesDonation donation = donationService.donate(
                requestDto.getDonorId(),
                requestDto.getCenterId(),
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getUnit(),
                requestDto.getQuantity()
        );
        return DailyNecessitiesDonationResponseDto.fromEntity(donation);
    }

    // 2. 관리자 승인
    @PatchMapping("/donation/{id}/approve")
    public DailyNecessitiesDonationResponseDto approveDonation(@PathVariable Long id) {
        DailyNecessitiesDonation donation = donationService.approve(id);
        return DailyNecessitiesDonationResponseDto.fromEntity(donation);
    }

    // 3. 관리자 거부
    @PatchMapping("/donation/{id}/reject")
    public DailyNecessitiesDonationResponseDto rejectDonation(@PathVariable Long id) {
        DailyNecessitiesDonation donation = donationService.reject(id);
        return DailyNecessitiesDonationResponseDto.fromEntity(donation);
    }

    // 4. 승인 대기 목록 조회
    @GetMapping("/donation/pending")
    public List<DailyNecessitiesDonationResponseDto> getPendingDonations() {
        return donationService.getPendingDonations()
                .stream()
                .map(DailyNecessitiesDonationResponseDto::fromEntity)
                .toList();
    }

    // 5. 사용자 본인 기부 내역 조회
    @GetMapping("/donation/my")
    public List<DailyNecessitiesDonationResponseDto> getMyDonations(@RequestParam Long userId) {
        return donationService.getMyDonations(userId)
                .stream()
                .map(DailyNecessitiesDonationResponseDto::fromEntity)
                .toList();
    }
}
