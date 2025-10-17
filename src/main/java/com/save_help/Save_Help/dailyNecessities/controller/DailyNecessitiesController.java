package com.save_help.Save_Help.dailyNecessities.controller;

import com.save_help.Save_Help.dailyNecessities.dto.*;
import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.service.*;
import io.swagger.v3.oas.annotations.Operation;
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
    private final DailyNecessitiesCenterMessageService messageService;
    private final DailyNecessitiesUserRequestMessageService userRequestMessageService;

    public DailyNecessitiesController(
            DailyNecessitiesService necessitiesService,
            UserNecessityRequestService requestService,
            DailyNecessitiesStockService stockService,
            DailyNecessitiesStatisticsService statisticsService,
            DailyNecessitiesReportService reportService,
            DailyNecessitiesDonationService donationService, DailyNecessitiesCenterMessageService messageService, DailyNecessitiesUserRequestMessageService userRequestMessageService) {
        this.necessitiesService = necessitiesService;
        this.requestService = requestService;
        this.stockService = stockService;
        this.statisticsService = statisticsService;
        this.reportService = reportService;
        this.donationService = donationService;
        this.messageService = messageService;
        this.userRequestMessageService = userRequestMessageService;
    }

    // ------------------------------------
    // 기본 CRUD
    // ------------------------------------

    @Operation(summary = "전체 생필품 조회", description = "등록된 모든 생필품 정보를 조회합니다.")
    @GetMapping("/getAll")
    public List<DailyNecessitiesDto> getAll() {
        return necessitiesService.getAll();
    }

    @Operation(summary = "ID로 생필품 조회", description = "생필품 ID를 통해 단건 조회합니다.")
    @GetMapping("/{id}")
    public DailyNecessitiesDto getById(@PathVariable Long id) {
        return necessitiesService.getById(id);
    }

    @Operation(summary = "카테고리별 생필품 조회", description = "카테고리 정보를 통해 해당 카테고리의 생필품 목록을 조회합니다.")
    @GetMapping("/category/{category}")
    public List<DailyNecessitiesDto> getByCategory(@PathVariable NecessityCategory category) {
        return necessitiesService.getByCategory(category);
    }

    @Operation(summary = "이름으로 생필품 검색", description = "입력한 이름으로 생필품을 검색합니다.")
    @GetMapping("/search")
    public List<DailyNecessitiesDto> searchByName(@RequestParam String name) {
        return necessitiesService.searchByName(name);
    }

    @Operation(summary = "생필품 등록", description = "새로운 생필품을 등록합니다.")
    @PostMapping("/create")
    public DailyNecessitiesDto create(@RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.create(dto);
    }

    @Operation(summary = "생필품 수정", description = "ID에 해당하는 생필품 정보를 수정합니다.")
    @PutMapping("/{id}")
    public DailyNecessitiesDto update(@PathVariable Long id, @RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.update(id, dto);
    }

    @Operation(summary = "생필품 삭제", description = "ID에 해당하는 생필품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        necessitiesService.delete(id);
    }

    // ------------------------------------
    // 재고 관리
    // ------------------------------------

    @Operation(summary = "재고 감소", description = "사용자가 생필품을 신청할 때 재고를 감소시킵니다.")
    @PostMapping("/{itemId}/reduce")
    public ResponseEntity<DailyNecessities> reduceStock(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(stockService.reduceStock(itemId, userId, quantity));
    }

    @Operation(summary = "사용자에 의한 재고 추가", description = "사용자가 생필품을 기부하거나 입고할 때 재고를 추가합니다.")
    @PostMapping("/{itemId}/add/user")
    public ResponseEntity<DailyNecessities> addStockByUser(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(stockService.addStock(itemId, userId, quantity));
    }

    @Operation(summary = "관리자에 의한 재고 추가", description = "관리자가 생필품 재고를 추가합니다.")
    @PostMapping("/{itemId}/add/admin")
    public ResponseEntity<DailyNecessities> addStockByAdmin(
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(necessitiesService.addStock(itemId, quantity));
    }

    @Operation(summary = "부족한 재고 조회", description = "부족한 생필품 재고를 조회합니다.")
    @GetMapping("/low-stock")
    public ResponseEntity<List<DailyNecessities>> getLowStockItems(
            @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(necessitiesService.getLowStockItems(threshold));
    }

    @Operation(summary = "생필품 히스토리 조회", description = "등록된 특정한 생필품 히스토리를 조회합니다.")
    @GetMapping("/history/item/{itemId}")
    public ResponseEntity<List<DailyNecessitiesHistory>> getHistoryByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(stockService.getHistoryByItem(itemId));
    }

    @Operation(summary = "생필품 유통기한 조회", description = "등록된 생필품의 유통기한을 조회합니다.")
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

    @Operation(summary = "생필품 지원처의 재고 수량을 조회", description = "생필품 지원처의 재고 수량을 조회합니다.")
    @GetMapping("/center")
    public ResponseEntity<List<StockStatisticsDto>> getStockByCenter() {
        return ResponseEntity.ok(statisticsService.getStockByCenter());
    }

    // ------------------------------------
    // 센터별 품목 조회
    // ------------------------------------
    @Operation(summary = "센터별 생필품 품목 조회", description = "센터별 생필품 품목들을 조회합니다")
    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<DailyNecessitiesDto>> getByCenter(@PathVariable Long centerId) {
        List<DailyNecessitiesDto> list = necessitiesService.getByCenter(centerId);
        return ResponseEntity.ok(list);
    }

    // ------------------------------------
    // 사용자 자동 신청
    // ------------------------------------

    @Operation(summary = "생필품 사용자 자동 신청", description = "생필품을 자동 신청 합니다")
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

    @Operation(summary = "사용자가 신청한 생필품 조회", description = "사용자가 신청한 생필품들을 조회합니다.")
    @GetMapping("/request/user/{userId}")
    public ResponseEntity<List<UserNecessityRequestResponseDto>> getRequestsByUser(@PathVariable Long userId) {
        List<UserNecessityRequestResponseDto> responses = requestService.getRequestsByUser(userId)
                .stream()
                .map(UserNecessityRequestResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "생필품 신청 상태 변경", description = "생필품 신청 상태를 변경합니다")
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
    @Operation(summary = "생필품 품목 승인이 pending된 품목들을 조회", description = "생필품 품목 승인이 pending된 품목들을 조회합니다")
    @GetMapping("/admin/pending-items")
    public ResponseEntity<List<DailyNecessities>> getPendingItems() {
        List<DailyNecessities> list = necessitiesService.getAllForAdmin().stream()
                .filter(item -> item.getApprovalStatus() == DailyNecessities.ApprovalStatus.PENDING)
                .toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "생필품 품목을 승인시킵니다", description = "생필품 품목을 승인시킵니다")
    @PatchMapping("/admin/{id}/approve")
    public ResponseEntity<String> approveItem(@PathVariable Long id) {
        necessitiesService.approveItem(id);
        return ResponseEntity.ok("품목 승인 완료");
    }

    @Operation(summary = "생필품 품목 거부", description = "생필품 품목을 거부합니다")
    @PatchMapping("/admin/{id}/reject")
    public ResponseEntity<String> rejectItem(@PathVariable Long id) {
        necessitiesService.rejectItem(id);
        return ResponseEntity.ok("품목 거부 완료");
    }

    // ------------------------------------
    // 사용자 기부 기능
    // ------------------------------------

    // 1. 기부 요청
    @Operation(summary = "사용자 기부 요청", description = "사용자가 생필품 지원처에 기부를 요청합니다")
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
    @Operation(summary = "관리자 기부 승인", description = "관리자가 기부를 승인합니다")
    @PatchMapping("/donation/{id}/approve")
    public DailyNecessitiesDonationResponseDto approveDonation(@PathVariable Long id) {
        DailyNecessitiesDonation donation = donationService.approve(id);
        return DailyNecessitiesDonationResponseDto.fromEntity(donation);
    }

    // 3. 관리자 거부
    @Operation(summary = "생필품 관리자 거부", description = "생필품 관리자 거부 기능 입니다.")
    @PatchMapping("/donation/{id}/reject")
    public DailyNecessitiesDonationResponseDto rejectDonation(@PathVariable Long id) {
        DailyNecessitiesDonation donation = donationService.reject(id);
        return DailyNecessitiesDonationResponseDto.fromEntity(donation);
    }

    // 4. 승인 대기 목록 조회
    @Operation(summary = "생필품 기부 승인 대기 목록 조회", description = "생필품 기부 승인 대기 목록을 조회합니다")
    @GetMapping("/donation/pending")
    public List<DailyNecessitiesDonationResponseDto> getPendingDonations() {
        return donationService.getPendingDonations()
                .stream()
                .map(DailyNecessitiesDonationResponseDto::fromEntity)
                .toList();
    }

    // 5. 사용자 본인 기부 내역 조회
    @Operation(summary = "사용자 본인 기부 내역 조회", description = "사용자 본인이 기부한 내역을 조회합니다")
    @GetMapping("/donation/my")
    public List<DailyNecessitiesDonationResponseDto> getMyDonations(@RequestParam Long userId) {
        return donationService.getMyDonations(userId)
                .stream()
                .map(DailyNecessitiesDonationResponseDto::fromEntity)
                .toList();
    }

    // ------------------------------------
    // 생필품 지원처가 사용자에게 생필품 지원 관련 메시지 전송
    // ------------------------------------

    // 센터 → 사용자 메시지 전송
    @Operation(summary = "센터에서 사용자에게 메시지 전송", description = "센터가 특정 사용자에게 생필품 관련 안내 메시지를 전송합니다.")
    @PostMapping("/centerMessages/send")
    public ResponseEntity<DailyNecessitiesCenterMessageDto> sendMessage(
            @RequestParam Long centerId,
            @RequestParam Long userId,
            @RequestParam String message
    ) {
        return ResponseEntity.ok(messageService.sendMessage(centerId, userId, message));
    }

    // 사용자 메시지 조회
    @Operation(summary = "사용자 메시지 조회", description = "사용자가 받은 메시지 목록을 조회합니다.")
    @GetMapping("/centerMessages/user/{userId}")
    public ResponseEntity<List<DailyNecessitiesCenterMessageDto>> getUserMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getMessagesForUser(userId));
    }

    // 메시지 읽음 처리
    @Operation(summary = "메시지 읽음 처리", description = "해당 메시지를 읽음 처리 상태로 변경합니다.")
    @PatchMapping("/centerMessages/{id}/read")
    public ResponseEntity<String> markMessageRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok("메시지를 읽음 처리했습니다.");
    }

    // ------------------------------------
    // 사용자가 생필품 지원처에 생필품 요청 메시지 전송
    // ------------------------------------

    // 1️. 사용자 요청 메시지 생성
    @Operation(summary = "사용자 요청 메시지 전송", description = "사용자가 생필품 지원처에 요청 메시지를 전송합니다.")
    @PostMapping("/user-requests/send")
    public ResponseEntity<DailyNecessitiesUserRequestMessageDto> sendRequest(
            @RequestParam Long userId,
            @RequestParam Long centerId,
            @RequestParam String message
    ) {
        return ResponseEntity.ok(userRequestMessageService.create(userId, centerId, message));
    }

    // 2. 센터가 받은 요청 조회
    @Operation(summary = "센터에서 받은 요청 조회", description = "센터가 받은 사용자 요청 메시지 목록을 조회합니다.")
    @GetMapping("/user-requests/center/{centerId}")
    public ResponseEntity<List<DailyNecessitiesUserRequestMessageDto>> getRequestsForCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(userRequestMessageService.getRequestsForCenter(centerId));
    }

    // 3️. 사용자가 보낸 요청 조회
    @Operation(summary = "사용자가 보낸 요청 조회", description = "사용자가 보낸 요청 메시지 목록을 조회합니다.")
    @GetMapping("/user-requests/user/{userId}")
    public ResponseEntity<List<DailyNecessitiesUserRequestMessageDto>> getRequestsMessageByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userRequestMessageService.getRequestsByUser(userId));
    }

    // 4️. 요청 처리 완료
    @Operation(summary = "요청 처리 완료", description = "센터에서 해당 요청 메시지를 처리 완료 상태로 변경합니다.")
    @PatchMapping("/user-requests/{id}/processed")
    public ResponseEntity<String> markAsProcessed(@PathVariable Long id) {
        userRequestMessageService.markAsProcessed(id);
        return ResponseEntity.ok("요청 처리 완료 상태로 변경했습니다.");
    }


}
