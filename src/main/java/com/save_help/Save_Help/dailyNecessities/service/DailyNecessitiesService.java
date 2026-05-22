package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesContactRequestCreateDto;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesDto;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesRequestCreateDto;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesRequestResponseDto;
import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessitiesCreated;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessityEligibilityEvent;
import com.save_help.Save_Help.dailyNecessities.kafka.producer.DailyNecessitiesPublisher;
import com.save_help.Save_Help.dailyNecessities.repository.*;
import com.save_help.Save_Help.dailyNecessities.spec.DailyNecessitiesSpecs;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DailyNecessitiesService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final CommunityCenterRepository centerRepository;
    private final UserNecessitiesRepository userNecessitiesRepository;
    private final DailyNecessitiesAlertService alertService;
    private final DailyNecessitiesRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final DailyNecessitiesPublisher dailyNecessitiesPublisher;
    private final DailyNecessitiesContactRequestRepository contactRequestRepository;
    private final DailyNecessityApplicationRepository applicationRepository;
    private final DailyNecessitiesPublisher dailyNecessityEligibilityProducer;


    public DailyNecessitiesService(DailyNecessitiesRepository necessitiesRepository,
                                   CommunityCenterRepository centerRepository, UserNecessitiesRepository userNecessitiesRepository, DailyNecessitiesAlertService alertService, DailyNecessitiesRequestRepository requestRepository, UserRepository userRepository, DailyNecessitiesPublisher dailyNecessitiesPublisher, DailyNecessitiesContactRequestRepository contactRequestRepository, DailyNecessityApplicationRepository applicationRepository, DailyNecessitiesPublisher dailyNecessityEligibilityProducer) {
        this.necessitiesRepository = necessitiesRepository;
        this.centerRepository = centerRepository;
        this.userNecessitiesRepository = userNecessitiesRepository;
        this.alertService = alertService;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.dailyNecessitiesPublisher = dailyNecessitiesPublisher;
        this.contactRequestRepository = contactRequestRepository;
        this.applicationRepository = applicationRepository;
        this.dailyNecessityEligibilityProducer = dailyNecessityEligibilityProducer;
    }

    // 생성
    public DailyNecessitiesDto create(DailyNecessitiesDto dto) {
        CommunityCenter center = centerRepository.findById(dto.getCenterId())
                .orElseThrow(() -> new EntityNotFoundException("CommunityCenter not found"));

        DailyNecessities item = new DailyNecessities(
                dto.getName(),
                dto.getCategory(),
                dto.getUnit(),
                dto.getStock(),
                dto.getExpirationDate(),
                center
        );

        DailyNecessities saved = necessitiesRepository.save(item);
        return DailyNecessitiesDto.fromEntity(saved);
    }

    // 전체 조회
    public List<DailyNecessitiesDto> getAll() {
        return necessitiesRepository.findByActiveTrue()
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    // 카테고리별 조회
    public List<DailyNecessitiesDto> getByCategory(DailyNecessitiesCategory category) {
        return necessitiesRepository.findByCategory(category)
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    // 이름 검색
    public List<DailyNecessitiesDto> searchByName(String name) {
        return necessitiesRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    // 수정
    public DailyNecessitiesDto update(Long id, DailyNecessitiesDto dto) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setUnit(dto.getUnit());
        item.setStock(dto.getStock());
        item.setExpirationDate(dto.getExpirationDate());

        DailyNecessities updated = necessitiesRepository.save(item);
        return DailyNecessitiesDto.fromEntity(updated);
    }

    // 삭제(비활성화)
    public void delete(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.deactivate();
        necessitiesRepository.save(item);
    }

    // 단건 조회
    public DailyNecessitiesDto getById(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("생필품을 찾을 수 없습니다."));
        return DailyNecessitiesDto.fromEntity(item);
    }


    // 관리자 거부
    public DailyNecessities rejectItem(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.reject();
        return necessitiesRepository.save(item);
    }

    // 사용자 조회 시 승인된 품목만 반환
    public List<DailyNecessities> getAllForUser() {
        return necessitiesRepository.findByApprovalStatus(DailyNecessities.ApprovalStatus.APPROVED);
    }

    // 재고 조회 (센터별)
    /*
    public List<DailyNecessities> getStockByCenter(Long centerId) {

        return necessitiesRepository.findByProvidedBy_Id(centerId)
                .stream()
                .filter(item -> item.getApprovalStatus() == DailyNecessities.ApprovalStatus.APPROVED)
                .toList();

    } */
    // 재고 조회 (전체)
    public List<DailyNecessities> getAllStock() {
        return necessitiesRepository.findByApprovalStatus(DailyNecessities.ApprovalStatus.APPROVED);
    }

    // 재고 입고
    public DailyNecessities addStock(Long itemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("입고 수량은 1 이상이어야 합니다.");
        DailyNecessities item = necessitiesRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다."));
        item.setStock(item.getStock() + quantity);
        return necessitiesRepository.save(item);
    }

    // 재고 출고
    public DailyNecessities reduceStock(Long itemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("출고 수량은 1 이상이어야 합니다.");
        DailyNecessities item = necessitiesRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다."));

        if (item.getStock() < quantity) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + item.getStock());
        }

        item.setStock(item.getStock() - quantity);
        return necessitiesRepository.save(item);
    }
    

    //관리자 승인
    public DailyNecessities approveItem(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.approve();
        return necessitiesRepository.save(item);
    }

    // 관리자 전체 조회 (모든 승인 상태 포함)
    public List<DailyNecessities> getAllForAdmin() {
        return necessitiesRepository.findAll();
    }


    // 재고 조회 (센터별)

    public List<DailyNecessities> getStockByCenter(Long centerId) {

        return necessitiesRepository.findByProvidedBy_Id(centerId)
                .stream()
                .filter(item -> item.getApprovalStatus() == DailyNecessities.ApprovalStatus.APPROVED)
                .toList();

    }

    // 재고 부족 품목 조회 (임계치 기준)
    public List<DailyNecessities> getLowStockItems(int threshold) {
        return necessitiesRepository.findByStockLessThanAndApprovalStatus(threshold, DailyNecessities.ApprovalStatus.APPROVED);
    }

    // 기부 승인 시 기존 품목이 있으면 반환, 없으면 생성
    public DailyNecessities findOrCreateItem(String name, DailyNecessitiesCategory category, String unit, CommunityCenter center) {
        return necessitiesRepository.findByNameAndProvidedBy_Id(name, center.getId())
                .orElseGet(() -> necessitiesRepository.save(new DailyNecessities(name, category, unit, 0, null, center)));
    }

    // 저장
    public DailyNecessities save(DailyNecessities item) {
        return necessitiesRepository.save(item);
    }

    public List<DailyNecessitiesDto> getByCenter(Long centerId) {
        List<DailyNecessities> necessitiesList = necessitiesRepository.findByProvidedBy_Id(centerId);
        return necessitiesList.stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    public List<DailyNecessitiesDto> getRecommendationsForUser(Long userId) {
        //1. 사용자의 최근 신청 이력 조회
        List<UserNecessityRequest> recentRequests = userNecessitiesRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        if (recentRequests.isEmpty()) {
            // 신청 이력이 없다면 인기 품목(전체 기준 상위 재고 or 최근 사용량 높은 품목) 추천
            List<DailyNecessities> popularItems = necessitiesRepository.findTop10ByOrderByRequestCountDesc();
            return popularItems.stream().map(DailyNecessitiesDto::fromEntity).toList();
        }

        // 2️. 사용자가 자주 신청한 카테고리 상위 7개 추출
        Map<DailyNecessitiesCategory, Long> categoryCount = recentRequests.stream()
                .collect(Collectors.groupingBy(
                        req -> req.getItem().getCategory(),
                        Collectors.counting()
                ));

        List<DailyNecessitiesCategory> topCategories = categoryCount.entrySet().stream()
                .sorted(Map.Entry.<DailyNecessitiesCategory, Long>comparingByValue().reversed())
                .limit(7)
                .map(Map.Entry::getKey)
                .toList();

        // 3. 상위 카테고리 중 재고가 충분한 품목 추천
        List<DailyNecessities> items = necessitiesRepository.findByCategoryInAndStockGreaterThan(topCategories, 0);

        // 4️. 최대 7개만 반환
        return items.stream()
                .limit(7)
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }


    public void alertLowStockItems() {
        alertService.sendLowStockAlerts();
    }


    public DailyNecessitiesRequestResponseDto create(DailyNecessitiesRequestCreateDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        CommunityCenter center = null;
        if (dto.getCenterId() != null) {
            center = centerRepository.findById(dto.getCenterId())
                    .orElseThrow(() -> new IllegalArgumentException("센터 없음"));
        }

        DailyNecessities item = null;
        if (dto.getItemId() != null) {
            item = necessitiesRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("품목 없음"));
        }

        // 최소 입력 검증: itemId가 없으면 itemName 필요
        if (item == null && (dto.getItemName() == null || dto.getItemName().isBlank())) {
            throw new IllegalArgumentException("itemId 또는 itemName 중 하나는 필수입니다.");
        }

        // 수량 기본값
        Integer qty = dto.getQuantity();
        if (qty == null || qty <= 0) qty = 1;

        DailyNecessitiesRequest req = new DailyNecessitiesRequest();
        req.setUser(user);
        req.setCenter(center);
        req.setItem(item);
        req.setItemName(item != null ? null : dto.getItemName());
        req.setQuantity(qty);
        req.setMessage(dto.getMessage());
        req.setContact(dto.getContact());
        req.setAddress(dto.getAddress());

        if (dto.getPriority() != null) req.setPriority(dto.getPriority());

        requestRepository.save(req);

        return DailyNecessitiesRequestResponseDto.from(req);
    }

    @Transactional(readOnly = true)
    public List<DailyNecessitiesRequestResponseDto> getByUser(Long userId) {
        return requestRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(DailyNecessitiesRequestResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DailyNecessitiesRequestResponseDto> getByCenter(Long centerId, DailyNecessitiesRequest.Status status) {
        List<DailyNecessitiesRequest> list =
                (status == null)
                        ? requestRepository.findByCenterIdOrderByCreatedAtDesc(centerId)
                        : requestRepository.findByCenterIdAndStatusOrderByCreatedAtDesc(centerId, status);

        return list.stream().map(DailyNecessitiesRequestResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<DailyNecessitiesRequestResponseDto> getByStatus(DailyNecessitiesRequest.Status status) {
        return requestRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(DailyNecessitiesRequestResponseDto::from)
                .toList();
    }

    public void updateStatus(Long requestId, DailyNecessitiesRequest.Status status, String adminNote) {
        DailyNecessitiesRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("긴급 요청 없음"));

        if (req.getStatus() == DailyNecessitiesRequest.Status.FULFILLED || req.getStatus() == DailyNecessitiesRequest.Status.REJECTED || req.getStatus() == DailyNecessitiesRequest.Status.CANCELLED) {
            throw new IllegalStateException("종료된 요청은 상태 변경 불가");
        }

        req.setStatus(status);
        req.setUpdatedAt(LocalDateTime.now());
        if (adminNote != null) req.setAdminNote(adminNote);

        // - status 변경 시 사용자에게 알림 발송
        // - 센터 담당자에게 할당 알림 등
    }

    public void cancelByUser(Long requestId, Long userId) {
        DailyNecessitiesRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("긴급 요청 없음"));

        if (!req.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인 요청만 취소할 수 있습니다.");
        }
        if (req.getStatus() == DailyNecessitiesRequest.Status.FULFILLED || req.getStatus() == DailyNecessitiesRequest.Status.REJECTED) {
            throw new IllegalStateException("이미 처리된 요청은 취소할 수 없습니다.");
        }

        req.setStatus(DailyNecessitiesRequest.Status.CANCELLED);
        req.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Page<DailyNecessitiesDto> searchItems(Long centerId,
                                                 DailyNecessitiesCategory category,
                                                 DailyNecessities.ApprovalStatus approvalStatus,
                                                 Boolean active,
                                                 String keyword,
                                                 Pageable pageable) {
        Specification<DailyNecessities> spec = Specification.<DailyNecessities>unrestricted()
                .and(DailyNecessitiesSpecs.hasCenterId(centerId))
                .and(DailyNecessitiesSpecs.hasCategory(category))
                .and(DailyNecessitiesSpecs.hasApprovalStatus(approvalStatus))
                .and(DailyNecessitiesSpecs.isActive(active))
                .and(DailyNecessitiesSpecs.containsKeyword(keyword));

        return necessitiesRepository.findAll(spec, pageable)
                .map(DailyNecessitiesDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<DailyNecessitiesDto> getByCenterAndCategory(Long centerId, DailyNecessitiesCategory category) {
        return necessitiesRepository.findByProvidedBy_IdAndCategory(centerId, category)
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DailyNecessitiesDto> getAvailableItems() {
        return necessitiesRepository
                .findByApprovalStatusAndActiveTrueAndStockGreaterThan(
                        DailyNecessities.ApprovalStatus.APPROVED, 0
                )
                .stream()
                .filter(item -> !item.isExpired())
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    // 센터별 사용 가능한 품목 조회
    @Transactional(readOnly = true)
    public List<DailyNecessitiesDto> getAvailableItemsByCenter(Long centerId) {
        return necessitiesRepository.findAvailableItemsByCenter(
                        centerId,
                        DailyNecessities.ApprovalStatus.APPROVED,
                        LocalDateTime.now()
                )
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    private DailyNecessities findEntity(Long id) {
        return necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("생필품을 찾을 수 없습니다. id=" + id));
    }

    public DailyNecessities create(DailyNecessities dailyNecessity) {
        DailyNecessities saved = necessitiesRepository.save(dailyNecessity);
        dailyNecessitiesPublisher.publishCreated(saved);
        return saved;
    }

    public List<DailyNecessities> getRecommendedDailyNecessities(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("IllegalArgumentException"));

    
        List<DailyNecessities> supports = necessitiesRepository.findByActiveTrue();

        List<DailyNecessities> result = new ArrayList<>();

        for (DailyNecessities support : supports) {

            result.add(support);
        }

        return result;
    }
    //public List<DailyNecessityApplication> getUserApplications(Long userId) {
    //    return necessitiesRepository.findByUser_Id(userId);
   // }

    public DailyNecessities getDailyNecessity(Long necessityId) {
        return necessitiesRepository.findById(necessityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 생필품이 존재하지 않습니다."));
    }

    @Transactional
    public DailyNecessitiesContactRequest createContactRequest(
            Long necessityId,
            DailyNecessitiesContactRequestCreateDto dto
    ) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        DailyNecessities dailyNecessities = necessitiesRepository.findById(necessityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 생필품이 존재하지 않습니다."));

        DailyNecessitiesContactRequest request = new DailyNecessitiesContactRequest(
                user,
                dailyNecessities,
                dto.getContactPhone(),
                dto.getMessage()
        );

        return contactRequestRepository.save(request);
    }

    @Transactional
    public void updateEligibilityCondition(
            Long necessityId,
            Long centerId,
            Integer incomeLevel,
            Boolean requireCheck
    ) {
        DailyNecessities necessity = necessitiesRepository.findById(necessityId)
                .orElseThrow(() -> new IllegalArgumentException("생필품을 찾을 수 없습니다."));

        necessity.updateEligibilityCondition(
                incomeLevel,
                requireCheck
        );

        DailyNecessityEligibilityEvent event =
                DailyNecessityEligibilityEvent.builder()
                        .necessityId(necessity.getId())
                        .centerId(necessity.getProvidedBy().getId())
                        .incomeLevel(incomeLevel)
                        .requireCheck(requireCheck)
                        .build();

        dailyNecessityEligibilityProducer.publishEligibilityChanged(event);
    }

    public DailyNecessities createDailyNecessity(DailyNecessities dailyNecessities) {

        DailyNecessities savedDailyNecessities =
                necessitiesRepository.save(dailyNecessities);

        DailyNecessitiesCreated event = DailyNecessitiesCreated.builder()
                .necessityId(savedDailyNecessities.getId())
                .title(savedDailyNecessities.getName())
                .category(savedDailyNecessities.getCategory().name())
                .stock(savedDailyNecessities.getStock())
                .build();

        dailyNecessityEligibilityProducer.publishDailyNecessitiesCreated(event);

        return savedDailyNecessities;
    }

    public List<DailyNecessityApplication> getAutoApplications(Long necessityId) {
        DailyNecessities necessity =
                necessitiesRepository.findById(necessityId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("생필품을 찾을 수 없습니다."));

        return applicationRepository
                .findBySupportIdAndApplyTypeOrderByAppliedAtDesc(
                        necessity.getId(),
                        "AUTO"
                );

    }
}
