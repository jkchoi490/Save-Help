package com.save_help.Save_Help.dailyNecessities.controller;

import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesDto;
import com.save_help.Save_Help.dailyNecessities.dto.StockStatisticsDto;
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
    public ResponseEntity<UserNecessityRequest> createRequest(
            @RequestParam Long userId,
            @RequestParam Long itemId,
            @RequestParam Integer quantity) {
        UserNecessityRequest request = requestService.createRequest(userId, itemId, quantity);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/request/user/{userId}")
    public ResponseEntity<List<UserNecessityRequest>> getRequestsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getRequestsByUser(userId));
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

    @PostMapping("/donation")
    public DailyNecessitiesDonation donate(@RequestParam Long userId,
                                           @RequestParam Long centerId,
                                           @RequestParam String name,
                                           @RequestParam NecessityCategory category,
                                           @RequestParam String unit,
                                           @RequestParam Integer quantity) {
        return donationService.donate(userId, centerId, name, category, unit, quantity);
    }

    @PatchMapping("/donation/{id}/approve")
    public DailyNecessitiesDonation approveDonation(@PathVariable Long id) {
        return donationService.approve(id);
    }

    @PatchMapping("/donation/{id}/reject")
    public DailyNecessitiesDonation rejectDonation(@PathVariable Long id) {
        return donationService.reject(id);
    }

    @GetMapping("/donation/pending")
    public List<DailyNecessitiesDonation> getPendingDonations() {
        return donationService.getPendingDonations();
    }

    @GetMapping("/donation/my")
    public List<DailyNecessitiesDonation> getMyDonations(@RequestParam Long userId) {
        return donationService.getMyDonations(userId);
    }
}
