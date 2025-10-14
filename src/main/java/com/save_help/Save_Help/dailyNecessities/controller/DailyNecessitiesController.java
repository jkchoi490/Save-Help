package com.save_help.Save_Help.dailyNecessities.controller;

import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesDto;
import com.save_help.Save_Help.dailyNecessities.dto.StockStatisticsDto;
import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
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

    public DailyNecessitiesController(DailyNecessitiesService necessitiesService, UserNecessityRequestService requestService, DailyNecessitiesStockService stockService, DailyNecessitiesStatisticsService statisticsService, DailyNecessitiesReportService reportService, DailyNecessitiesDonationService donationService) {
        this.necessitiesService = necessitiesService;
        this.requestService = requestService;
        this.stockService = stockService;
        this.statisticsService = statisticsService;
        this.reportService = reportService;
        this.donationService = donationService;
    }


    @Operation(summary = "생필품 전체 목록 조회", description = "생필품 전체 목록을 조회합니다")
    @GetMapping("/getAll")
    public List<DailyNecessitiesDto> getAll() {
        return necessitiesService.getAll();
    }

    @Operation(summary = "생필품 단건 조회", description = "ID로 특정 생필품을 조회합니다")
    @GetMapping("/{id}")
    public DailyNecessitiesDto getById(@PathVariable Long id) {
        return necessitiesService.getById(id);
    }

    @Operation(summary = "생필품 카테고리별 조회", description = "생필품을 카테고리별로 조회합니다")
    @GetMapping("/category/{category}")
    public List<DailyNecessitiesDto> getByCategory(@PathVariable NecessityCategory category) {
        return necessitiesService.getByCategory(category);
    }

    @Operation(summary = "생필품 이름 검색", description = "생필품을 이름으로 검색합니다")
    @GetMapping("/search")
    public List<DailyNecessitiesDto> searchByName(@RequestParam String name) {
        return necessitiesService.searchByName(name);
    }

    @Operation(summary = "생필품 생성", description = "생필품을 생성합니다")
    @PostMapping
    public DailyNecessitiesDto create(@RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.create(dto);
    }

    @Operation(summary = "생필품 수정", description = "생필품을 수정합니다")
    @PutMapping("/{id}")
    public DailyNecessitiesDto update(@PathVariable Long id, @RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.update(id, dto);
    }

    @Operation(summary = "생필품 비활성화", description = "생필품을 비활성화합니다")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        necessitiesService.delete(id);
    }

    @Operation(summary = "센터의 생필품 목록 조회", description = "특정 센터에 등록된 모든 생필품을 조회합니다.")
    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<DailyNecessitiesDto>> getByCenter(@PathVariable Long centerId) {
        List<DailyNecessitiesDto> list = necessitiesService.getByCenter(centerId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "사용자 생필품 자동 신청")
    @PostMapping
    public ResponseEntity<UserNecessityRequest> createRequest(
            @RequestParam Long userId,
            @RequestParam Long itemId,
            @RequestParam Integer quantity) {
        UserNecessityRequest request = requestService.createRequest(userId, itemId, quantity);
        return ResponseEntity.ok(request);
    }

    @Operation(summary = "사용자 신청 내역 조회")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserNecessityRequest>> getRequestsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getRequestsByUser(userId));
    }

    @Operation(summary = "관리자용 신청 상태 변경")
    @PatchMapping("/{requestId}")
    public ResponseEntity<String> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestParam UserNecessityRequest.RequestStatus status) {
        requestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok("신청 상태가 변경되었습니다.");
    }

    @Operation(summary = "관리자: 승인 대기 품목 전체 조회")
    @GetMapping("/pending")
    public ResponseEntity<List<DailyNecessities>> getPendingItems() {
        List<DailyNecessities> list = necessitiesService.getAllForAdmin()
                .stream()
                .filter(item -> item.getApprovalStatus() == DailyNecessities.ApprovalStatus.PENDING)
                .toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "관리자: 품목 승인")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<String> approveItem(@PathVariable Long id) {
        necessitiesService.approveItem(id);
        return ResponseEntity.ok("품목 승인 완료");
    }

    @Operation(summary = "관리자: 품목 거부")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<String> rejectItem(@PathVariable Long id) {
        necessitiesService.rejectItem(id);
        return ResponseEntity.ok("품목 거부 완료");
    }

    /*
    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<DailyNecessities>> getStockByCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(necessitiesService.getStockByCenter(centerId));
    }*/

    @GetMapping("/all")
    public ResponseEntity<List<DailyNecessities>> getAllStock() {
        return ResponseEntity.ok(necessitiesService.getAllStock());
    }

    @PostMapping("/{itemId}/add")
    public ResponseEntity<DailyNecessities> addStock(@PathVariable Long itemId, @RequestParam int quantity) {
        return ResponseEntity.ok(necessitiesService.addStock(itemId, quantity));
    }

    @PostMapping("/{itemId}/reduce")
    public ResponseEntity<DailyNecessities> reduceStock(@PathVariable Long itemId, @RequestParam int quantity) {
        return ResponseEntity.ok(necessitiesService.reduceStock(itemId, quantity));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<DailyNecessities>> getLowStockItems(@RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(necessitiesService.getLowStockItems(threshold));
    }

    @PostMapping("/{itemId}/add")
    public ResponseEntity<DailyNecessities> addStock(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(stockService.addStock(itemId, userId, quantity));
    }

    @PostMapping("/{itemId}/reduce")
    public ResponseEntity<DailyNecessities> reduceStock(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(stockService.reduceStock(itemId, userId, quantity));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<DailyNecessitiesHistory>> getHistoryByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(stockService.getHistoryByItem(itemId));
    }

    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<DailyNecessitiesHistory>> getHistoryByCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(stockService.getHistoryByCenter(centerId));
    }

    @GetMapping("/period")
    public ResponseEntity<List<DailyNecessitiesHistory>> getHistoryByPeriod(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        return ResponseEntity.ok(stockService.getHistoryByPeriod(startTime, endTime));
    }


    @GetMapping("/center")
    public ResponseEntity<List<StockStatisticsDto>> getStockByCenter() {
        return ResponseEntity.ok(statisticsService.getStockByCenter());
    }

    /*
    @GetMapping("/category")
    public ResponseEntity<List<StockStatisticsDto>> getStockByCategory() {
        return ResponseEntity.ok(statisticsService.getStockByCategory());
    }

     */

    /*
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReport(
            @RequestParam(required = false) Long centerId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) throws Exception {

        ByteArrayInputStream stream = reportService.generateReport(
                centerId,
                startDate != null ? LocalDate.parse(startDate) : null,
                endDate != null ? LocalDate.parse(endDate) : null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=DailyNecessitiesReport.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream.readAllBytes());
    }

     */

    // 사용자 기부 요청
    @PostMapping("/donate")
    public DailyNecessitiesDonation donate(@RequestParam Long userId,
                                           @RequestParam Long centerId,
                                           @RequestParam String name,
                                           @RequestParam NecessityCategory category,
                                           @RequestParam String unit,
                                           @RequestParam Integer quantity) {
        return donationService.donate(userId, centerId, name, category, unit, quantity);
    }

    // 관리자 승인
    @PatchMapping("/{id}/approve")
    public DailyNecessitiesDonation approve(@PathVariable Long id) {
        return donationService.approve(id);
    }

    // 관리자 거부
    @PatchMapping("/{id}/reject")
    public DailyNecessitiesDonation reject(@PathVariable Long id) {
        return donationService.reject(id);
    }

    // 사용자 본인 기부 내역 조회
    @GetMapping("/my")
    public List<DailyNecessitiesDonation> getMyDonations(@RequestParam Long userId) {
        return donationService.getMyDonations(userId);
    }

    // 관리자 승인 대기 목록 조회
    @GetMapping("/pending")
    public List<DailyNecessitiesDonation> getPendingDonations() {
        return donationService.getPendingDonations();
    }
}
