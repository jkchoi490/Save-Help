package com.save_help.Save_Help.nationalSubsidy.service;


import com.save_help.Save_Help.nationalSubsidy.dto.*;
import com.save_help.Save_Help.nationalSubsidy.entity.*;
import com.save_help.Save_Help.nationalSubsidy.kafka.ApplicationCreatedInternalEvent;
import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidyApplicationStatus;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.SubsidyCreatedInternalEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserNationalSubsidyEligibilityEvent;
import com.save_help.Save_Help.nationalSubsidy.repository.*;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NationalSubsidyService {

    private final NationalSubsidyRepository subsidyRepository;
    private final SubsidyApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final NationalSubsidyNotificationRepository notificationRepository;
    private final NationalSubsidyRepository nationalSubsidyRepository;
    private final SubsidyApplicationRepository subsidyApplicationRepository;
    private final NationalSubsidySubscriptionRepository subscriptionRepository;
    private final NationalSubsidyApplicationRepository appRepository;
    private final ApplicationEventPublisher publisher;



    public NationalSubsidyResponseDto create(NationalSubsidyRequestDto dto) {
        NationalSubsidy subsidy = new NationalSubsidy();
        updateEntityFromDto(subsidy, dto);
        NationalSubsidy saved = subsidyRepository.save(subsidy);
        publisher.publishEvent(new SubsidyCreatedInternalEvent(saved.getId()));
        return toResponseDto(saved);
    }

    public List<NationalSubsidyResponseDto> findAll() {
        return subsidyRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public NationalSubsidyResponseDto findById(Long id) {
        NationalSubsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보조금을 찾을 수 없습니다."));
        return toResponseDto(subsidy);
    }

    public List<NationalSubsidyResponseDto> findByType(NationalSubsidyType type) {
        return subsidyRepository.findByType(type).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<NationalSubsidyResponseDto> searchByName(String keyword) {
        return subsidyRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public NationalSubsidyResponseDto update(Long id, NationalSubsidyRequestDto dto) {
        NationalSubsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보조금을 찾을 수 없습니다."));
        updateEntityFromDto(subsidy, dto);
        return toResponseDto(subsidy);
    }

    public void delete(Long id) {
        subsidyRepository.deleteById(id);
    }

    private void updateEntityFromDto(NationalSubsidy subsidy, NationalSubsidyRequestDto dto) {
        subsidy.setName(dto.getName());
        subsidy.setDescription(dto.getDescription());
        subsidy.setType(dto.getType());
        subsidy.setCenter(dto.getCenter());
        subsidy.setMaxAmount(dto.getMaxAmount());
        subsidy.setTargetGroup(dto.getTargetGroup());
        subsidy.setStartDate(dto.getStartDate());
        subsidy.setEndDate(dto.getEndDate());
        subsidy.setActive(dto.isActive());
        subsidy.setMinAge(dto.getMinAge());
        subsidy.setMaxAge(dto.getMaxAge());
        subsidy.setIncomeLevel(dto.getIncomeLevel());
        subsidy.setDisabilityRequired(dto.getDisabilityRequired());
        subsidy.setEmergencyOnly(dto.getEmergencyOnly());
    }

    private NationalSubsidyResponseDto toResponseDto(NationalSubsidy s) {
        return NationalSubsidyResponseDto.builder()
                .id(s.getId())
                .name(s.getName())
                .description(s.getDescription())
                .type(s.getType())
                .center(s.getCenter())
                .maxAmount(s.getMaxAmount())
                .targetGroup(s.getTargetGroup())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .active(s.isActive())
                .minAge(s.getMinAge())
                .maxAge(s.getMaxAge())
                .incomeLevel(s.getIncomeLevel())
                .disabilityRequired(s.getDisabilityRequired())
                .emergencyOnly(s.getEmergencyOnly())
                .build();
    }

    public List<NationalSubsidy> findEligibleSubsidies(User user) {
        return subsidyRepository.findAll().stream()
                .filter(NationalSubsidy::isActive)
                .filter(subsidy -> subsidy.getMinAge() == null || user.getAge() >= subsidy.getMinAge())
                .filter(subsidy -> subsidy.getMaxAge() == null || user.getAge() <= subsidy.getMaxAge())
                .filter(subsidy -> subsidy.getIncomeLevel() == null || subsidy.getIncomeLevel().equals(user.getIncomeLevel()))
                .filter(subsidy -> subsidy.getDisabilityRequired() == null || subsidy.getDisabilityRequired().equals(user.isDisabled()))
                .filter(subsidy -> subsidy.getEmergencyOnly() == null || subsidy.getEmergencyOnly().equals(user.isInEmergency()))
                .toList();
    }


    public void autoApplyForUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<NationalSubsidy> eligibleSubsidies = findEligibleSubsidies(user);

        for (NationalSubsidy subsidy : eligibleSubsidies) {
            boolean alreadyApplied = applicationRepository
                    .findByUserIdAndSubsidyId(user.getId(), subsidy.getId())
                    .isPresent();

            if (!alreadyApplied) {
                SubsidyApplication application = new SubsidyApplication();
                application.setUser(user);
                application.setSubsidy(subsidy);
                applicationRepository.save(application);
            }
        }
    }

    public void autoApplyForAllUsers() {
        userRepository.findAll().forEach(user -> autoApplyForUser(user.getId()));
    }


    // 보조금 맞춤형 추천
    public List<NationalSubsidyResponseDto> recommend(Integer age, String incomeLevel, Boolean disability, Boolean emergency) {
        List<NationalSubsidy> all = subsidyRepository.findAll();
        return all.stream()
                .filter(s -> s.isActive())
                .filter(s -> (s.getMinAge() == null || age >= s.getMinAge()))
                .filter(s -> (s.getMaxAge() == null || age <= s.getMaxAge()))
                .filter(s -> (incomeLevel == null || s.getIncomeLevel() == null || s.getIncomeLevel().equalsIgnoreCase(incomeLevel)))
                .filter(s -> (disability == null || !s.getDisabilityRequired() || disability))
                .filter(s -> (emergency == null || !s.getEmergencyOnly() || emergency))
                .map(NationalSubsidyResponseDto::fromEntity)
                .toList();
    }

    // 신청 가능 보조금
    public List<NationalSubsidyResponseDto> findAvailableSubsidies() {
        LocalDate today = LocalDate.now();
        return subsidyRepository.findAvailableSubsidies(today)
                .stream()
                .map(NationalSubsidyResponseDto::fromEntity)
                .toList();
    }

    // 세부 필터 검색
    public List<NationalSubsidyResponseDto> filter(NationalSubsidyType type, String incomeLevel, Integer minAge,
                                                   Integer maxAge, Boolean disabilityRequired) {
        return subsidyRepository.filter(type, incomeLevel, minAge, maxAge, disabilityRequired)
                .stream()
                .map(NationalSubsidyResponseDto::fromEntity)
                .toList();
    }

    // 유저 신청 내역 조회
    public List<NationalSubsidyResponseDto> findApplicationsByUser(Long userId) {
        List<SubsidyApplication> apps = applicationRepository.findByUser_Id(userId);
        return apps.stream()
                .map(app -> NationalSubsidyResponseDto.fromEntity(app.getSubsidy()))
                .toList();
    }

    // 통계 API (센터별 / 유형별)
    public Map<String, Object> getStatistics() {
        List<NationalSubsidy> all = subsidyRepository.findAll();

        Map<String, Long> byCenter = all.stream()
                .collect(Collectors.groupingBy(NationalSubsidy::getCenter, Collectors.counting()));

        Map<NationalSubsidyType, Long> byType = all.stream()
                .collect(Collectors.groupingBy(NationalSubsidy::getType, Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", all.size());
        stats.put("byCenter", byCenter);
        stats.put("byType", byType);
        return stats;
    }

    //  CSV Export
    public Resource exportToCsv() {
        List<NationalSubsidy> subsidies = subsidyRepository.findAll();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            writer.write("보조금ID,보조금이름,타입,센터명,최대지원금액,보조금지급시작일,보조금지급종료일,보조금활성화여부\n");
            for (NationalSubsidy s : subsidies) {
                writer.write(String.format("%d,%s,%s,%s,%d,%s,%s,%b\n",
                        s.getId(),
                        s.getName(),
                        s.getType(),
                        s.getCenter(),
                        Optional.ofNullable(s.getMaxAmount()).orElse(0),
                        s.getStartDate(),
                        s.getEndDate(),
                        s.isActive()));
            }
            writer.flush();
            return new ByteArrayResource(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("CSV 생성 실패", e);
        }
    }

    // 보조금 활성/비활성 상태 변경
    public void updateStatus(Long id, boolean active) {
        NationalSubsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 보조금이 존재하지 않습니다. id=" + id));

        subsidy.setActive(active);
        subsidyRepository.save(subsidy);
    }


    @Transactional
    public Long apply(Long userId, Long subsidyId) {
        User user = userRepository.findById(userId).orElseThrow();
        NationalSubsidy subsidy = nationalSubsidyRepository.findById(subsidyId).orElseThrow();

        // (선택) 최소한의 신청 가능 검증
        if (!subsidy.isActive()) {
            throw new IllegalStateException("비활성화된 보조금입니다.");
        }

        SubsidyApplication app = new SubsidyApplication();
        app.setUser(user);
        app.setSubsidy(subsidy);
        app.setStatus(NationalSubsidyApplicationStatus.valueOf("PENDING")); // 접수됨(신청 완료)
        SubsidyApplication saved = subsidyApplicationRepository.save(app);

        // 신청 완료 알림 저장
        NationalSubsidyNotification n = new NationalSubsidyNotification();
        n.setUser(user);
        n.setTitle("보조금 신청 완료");
        n.setMessage("'" + subsidy.getName() + "' 신청이 완료(접수)되었습니다.");
        notificationRepository.save(n);

        return saved.getId();
    }

    // 보조금 자동 신청 전 정보 알림

    public NationalSubsidySubscriptionResponseDto subscribe(
            Long userId,
            NationalSubsidySubscriptionRequestDto dto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        NationalSubsidySubscription sub = new NationalSubsidySubscription();
        sub.setUser(user);
        sub.setMinAge(dto.getMinAge());
        sub.setMaxAge(dto.getMaxAge());
        sub.setIncomeLevel(dto.getIncomeLevel());
        sub.setDisability(dto.getDisability());
        sub.setEmergency(dto.getEmergency());
        sub.setType(dto.getType());
        sub.setRegion(dto.getRegion());

        subscriptionRepository.save(sub);

        return toDto(sub);
    }

    @Transactional(readOnly = true)
    public List<NationalSubsidySubscriptionResponseDto> getMySubscriptions(Long userId) {
        return subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void unsubscribe(Long subscriptionId) {
        NationalSubsidySubscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("구독 없음"));
        sub.setActive(false);
    }

    private NationalSubsidySubscriptionResponseDto toDto(NationalSubsidySubscription s) {
        return NationalSubsidySubscriptionResponseDto.builder()
                .subscriptionId(s.getId())
                .minAge(s.getMinAge())
                .maxAge(s.getMaxAge())
                .incomeLevel(s.getIncomeLevel())
                .disability(s.getDisability())
                .emergency(s.getEmergency())
                .type(s.getType())
                .region(s.getRegion())
                .active(s.isActive())
                .createdAt(s.getCreatedAt())
                .build();
    }

    //보조금 자동 신청
    @Transactional
    public void applyForSubsidyToUsers(Long subsidyId) {
        NationalSubsidy subsidy = subsidyRepository.findById(subsidyId).orElseThrow();
        if (!isSubsidyRunnable(subsidy)) return;

        int page = 0;
        int size = 500;

        while (true) {
            var users = userRepository.findEligibleUsersForSubsidy(
                    subsidy.getMinAge(),
                    subsidy.getMaxAge(),
                    subsidy.getIncomeLevel(),
                    Boolean.TRUE.equals(subsidy.getDisabilityRequired()),
                    Boolean.TRUE.equals(subsidy.getEmergencyOnly()),
                    PageRequest.of(page, size)
            );

            for (User u : users.getContent()) {
                tryInsert(u, subsidy, "Matched by subsidy event");
            }

            if (!users.hasNext()) break;
            page++;
        }
    }

    @Transactional
    public void applyForUserToSubsidies(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        int page = 0;
        int size = 200;
        LocalDate today = LocalDate.now();

        while (true) {
            var subsidies = subsidyRepository.findEligibleSubsidiesForUser(
                    user.getAge(),
                    user.getIncomeLevel(),
                    user.isDisabled(),
                    user.isInEmergency(),
                    today,
                    PageRequest.of(page, size)
            );

            for (NationalSubsidy s : subsidies.getContent()) {
                if (!isSubsidyRunnable(s)) continue;
                tryInsert(user, s, "Matched by user event");
            }

            if (!subsidies.hasNext()) break;
            page++;
        }
    }

    private boolean isSubsidyRunnable(NationalSubsidy s) {
        if (!s.isActive()) return false;

        LocalDate today = LocalDate.now();
        if (s.getStartDate() != null && s.getStartDate().isAfter(today)) return false;
        if (s.getEndDate() != null && s.getEndDate().isBefore(today)) return false;

        return true;
    }

    private void tryInsert(User u, NationalSubsidy s, String reason) {
        try {
            NationalSubsidyApplication saved =appRepository.save(
                    NationalSubsidyApplication.builder()
                            .user(u)
                            .subsidy(s)
                            .status(NationalSubsidyApplication.Status.APPLIED)
                            .reason(reason)
                            .build()
            );
            publisher.publishEvent(new ApplicationCreatedInternalEvent(
                    saved.getId(),
                    u.getId(),
                    s.getId()
            ));
        } catch (DataIntegrityViolationException dup) {
            // 유니크 충돌 -> 이미 신청완료 적재됨 -> 멱등 OK
        }


    }

    @Transactional
    public Long create(NationalSubsidy s) {
        NationalSubsidy saved = subsidyRepository.save(s);

        // 활성화된 지원금만 이벤트 발행하고 싶다면 if(saved.isActive())로 감싸도 됨
        publisher.publishEvent(new SubsidyCreatedInternalEvent(saved.getId()));
        return saved.getId();
    }


    @Transactional
    public int autoApplyForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        LocalDate today = LocalDate.now();

        List<NationalSubsidy> candidates =
                nationalSubsidyRepository.findOpenCandidates(today, user.getAge(), user.getIncomeLevel());

        int created = 0;
        for (NationalSubsidy s : candidates) {
            created += createIfEligible(user, s) ? 1 : 0;
        }
        return created;
    }

    @Transactional
    public int autoApplyForSubsidy(Long subsidyId) {
        NationalSubsidy subsidy = nationalSubsidyRepository.findById(subsidyId).orElseThrow();


        List<User> users = userRepository.findCandidateUsersForSubsidy(
                subsidy.getMinAge(), subsidy.getMaxAge(), subsidy.getIncomeLevel()
        );

        int created = 0;
        for (User u : users) {
            created += createIfEligible(u, subsidy) ? 1 : 0;
        }
        return created;
    }

    private boolean createIfEligible(User user, NationalSubsidy subsidy) {
        if (!isEligible(user, subsidy)) return false;

        if (subsidyApplicationRepository.existsByUser_IdAndSubsidy_Id(user.getId(), subsidy.getId())) return false;

        try {
            subsidyApplicationRepository.save(SubsidyApplication.builder()
                    .user(user)
                    .subsidy(subsidy)
                    .status(NationalSubsidyApplicationStatus.SUBMITTED)
                    .build());
            return true;
        } catch (org.springframework.dao.DataIntegrityViolationException e) {

            return false;
        }
    }

    private boolean isEligible(User u, NationalSubsidy s) {
        LocalDate today = LocalDate.now();
        if (!s.isActive() || !s.isOpen()) return false;
        if (s.getStartDate().isAfter(today) || s.getEndDate().isBefore(today)) return false;

        Integer minAge = s.getMinAge();
        Integer maxAge = s.getMaxAge();
        if (minAge != null && u.getAge() < minAge) return false;
        if (maxAge != null && u.getAge() > maxAge) return false;

        if (s.getIncomeLevel() != null && !s.getIncomeLevel().equals(u.getIncomeLevel())) return false;

        if (Boolean.TRUE.equals(s.getDisabilityRequired()) && !u.isDisabled()) return false;
        if (Boolean.TRUE.equals(s.getEmergencyOnly()) && !u.isInEmergency()) return false;

        return true;
    }

    public int handleSubsidyChanged(Long subsidyId, String reason) {
        LocalDate today = LocalDate.now();
        if (!subsidyRepository.existsRunnable(subsidyId, today)) {
            log.info("[AutoApply][Subsidy] subsidyId={} is not runnable today -> skip", subsidyId);
            return 0;
        }

        NationalSubsidy subsidy = subsidyRepository.findById(subsidyId).orElseThrow();

        int page = 0;
        int size = 700;
        int created = 0;
        int processed = 0;

        while (true) {
            var users = userRepository.findEligibleUsersForSubsidy(
                    subsidy.getMinAge(),
                    subsidy.getMaxAge(),
                    subsidy.getIncomeLevel(),
                    Boolean.TRUE.equals(subsidy.getDisabilityRequired()),
                    Boolean.TRUE.equals(subsidy.getEmergencyOnly()),
                    PageRequest.of(page, size)
            );

            for (User u : users.getContent()) {
                if (isEligible(u, subsidy)) {
                    tryInsert(u, subsidy, reason);
                    created++;
                }
                processed++;

            }

            if (!users.hasNext()) break;
            page++;
        }

        log.info("[AutoApply][Subsidy] subsidyId={}, created={}, processed={}", subsidyId, created, processed);
        return created;
    }

    @Transactional
    public void updateOpen(Long id, boolean open) {
        NationalSubsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 보조금이 존재하지 않습니다. id=" + id));

        subsidy.setOpen(open);
    }

    @Transactional(readOnly = true)
    public Page<NationalSubsidyResponseDto> searchAdmin(
            NationalSubsidyType type,
            String center,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    ) {
        return subsidyRepository.searchAdmin(type, center, from, to, pageable)
                .map(this::toResponseDto);
    }

    public void openSubsidy(Long subsidyId) {
        NationalSubsidy s = subsidyRepository.findById(subsidyId)
                .orElseThrow(() -> new IllegalArgumentException("보조금에 대한 예외 id=" + subsidyId));
        s.open();
    }

    @Transactional(readOnly = true)
    public Page<NationalSubsidyResponseDto> findAll(Pageable pageable) {
        return subsidyRepository.findAll(pageable).map(this::toResponseDto);
    }

    private LocalDate today() {
        return LocalDate.now();
    }


    @Transactional
    public void activate(Long id) {
        NationalSubsidy subsidy = nationalSubsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("IllegalArgumentException"));

        subsidy.activate();

        nationalSubsidyRepository.save(subsidy);
    }

    @Transactional(readOnly = true)
    public long countRunnable() {
        LocalDate today = LocalDate.now();
        return nationalSubsidyRepository.findRunnableSubsidies(today).size();
    }

    @Transactional
    public void approveApplication(Long applicationId) {
        NationalSubsidyApplication app = appRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 없습니다. id=" + applicationId));

        app.approve("관리자 승인");
    }

    @Transactional(readOnly = true)
    public Page<NationalSubsidyApplicationResponseDto> findAutoApplicationsByUser(Long userId, Pageable pageable) {
        return appRepository.findByUser_IdAndAppliedByOrderByCreatedAtDesc(
                        userId,
                        NationalSubsidyApplication.AppliedBy.AUTO,
                        pageable
                )
                .map(NationalSubsidyApplicationResponseDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public long countApplicationsBySubsidy(Long subsidyId) {
        return appRepository.countBySubsidy_Id(subsidyId);
    }

    @Transactional(readOnly = true)
    public Page<NationalSubsidyApplicationResponseDto> findApplicationsBySubsidy(Long subsidyId, Pageable pageable) {
        return appRepository.findBySubsidy_IdOrderByCreatedAtDesc(subsidyId, pageable)
                .map(NationalSubsidyApplicationResponseDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<NationalSubsidyApplicationResponseDto> findApplicationsByPeriod(
            LocalDate from,
            LocalDate to,
            PageRequest pageRequest
    ) {
        return appRepository.findCreatedBetween(
                from.atStartOfDay(),
                to.plusDays(1).atStartOfDay().minusNanos(1),
                pageRequest
        ).map(NationalSubsidyApplicationResponseDto::fromEntity);
    }

    private NationalSubsidy getSubsidy(Long subsidyId) {
        return subsidyRepository.findById(subsidyId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "보조금을 찾을 수 없습니다. subsidyId=" + subsidyId
                        )
                );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getBudgetStatus(Long subsidyId) {
        NationalSubsidy subsidy = subsidyRepository.findById(subsidyId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "보조금을 찾을 수 없습니다. id=" + subsidyId
                        )
                );

        long budget = subsidy.getCurrentBudgetAmount();
        long executed = subsidy.getExecutedAmount();
        long remaining = Math.max(budget - executed, 0L);

        double executionRate = budget == 0
                ? 0.0
                : (double) executed / budget * 100.0;

        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("subsidyId", subsidyId);
        result.put("currentBudgetAmount", budget);
        result.put("executedAmount", executed);
        result.put("remainingBudgetAmount", remaining);
        result.put("executionRate", executionRate);

        return result;
    }

    private void createApplicationNotification(
            User user,
            NationalSubsidy subsidy
    ) {
        NationalSubsidyNotification notification =
                new NationalSubsidyNotification();

        notification.setUser(user);
        notification.setTitle("보조금 신청 완료");
        notification.setMessage(
                "'" + subsidy.getName() + "' 신청이 완료되었습니다."
        );

        notificationRepository.save(notification);
    }

    /*
    @Transactional(readOnly = true)
    public Optional findAvailableSubsidies(
            Pageable pageable
    ) {
        return subsidyRepository
                .findApplicableSubsidiesWithBudget(
                        LocalDate.now(),
                        pageable
                )
                .map(this::toResponseDto);
    }

     */

    // 국가보조금 자동신청 기능
    @Transactional
    public int autoApply(
            UserNationalSubsidyEligibilityEvent event
    ) {

        // 1. User 조회
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다. userId="
                                        + event.getUserId()
                        )
                );

        log.info(
                "국가보조금 자동 신청 사용자 조회 완료: userId={}",
                user.getId()
        );

        // 2. 사용자 자동 신청 동의 여부 검사
        if (!user.isAutoApplyEnabled()) {

            log.info(
                    "자동 신청 비활성 사용자: userId={}",
                    user.getId()
            );

            return 0;
        }

        LocalDate today = LocalDate.now();

        // 3. 현재 신청 가능한 열린 보조금 조회
        List<NationalSubsidy> subsidies =
                nationalSubsidyRepository.findOpenSubsidies(today);

        log.info(
                "자동 신청 검사 대상 보조금 수: userId={}, count={}",
                user.getId(),
                subsidies.size()
        );

        int applicationCount = 0;

        // 4. 열린 보조금 순회
        for (NationalSubsidy subsidy : subsidies) {

            // 5. 기간 및 활성 상태 검사
            if (!subsidy.isRunnable(today)) {

                log.debug(
                        "신청 기간 또는 활성 상태 미충족: subsidyId={}",
                        subsidy.getId()
                );

                continue;
            }

            // 6. 기존 신청 여부 검사
            boolean alreadyApplied =
                    appRepository
                            .existsByUser_IdAndSubsidy_Id(
                                    user.getId(),
                                    subsidy.getId()
                            );

            if (alreadyApplied) {

                log.debug(
                        "이미 신청한 보조금: userId={}, subsidyId={}",
                        user.getId(),
                        subsidy.getId()
                );

                continue;
            }

            // 7. 사용자 자격 검사
            boolean eligible =
                    subsidy.isEligible(
                            user.getAge(),
                            user.getIncomeLevel(),
                            user.isDisabled(),
                            user.isInEmergency()
                    );

            if (!eligible) {

                log.debug(
                        "보조금 신청 자격 미충족: userId={}, subsidyId={}",
                        user.getId(),
                        subsidy.getId()
                );

                continue;
            }

            // 8. NationalSubsidyApplication 생성
            NationalSubsidyApplication application =
                    NationalSubsidyApplication.builder()
                            .user(user)
                            .subsidy(subsidy)
                            .status(
                                    NationalSubsidyApplication.Status.PENDING
                            )
                            .active(true)
                            .appliedBy(
                                    NationalSubsidyApplication.AppliedBy.AUTO
                            )
                            .eventId(event.getEventId())
                            .build();

            // 9. AUTO 신청 처리
            application.markAppliedAuto(
                    "사용자 조건 충족에 따른 국가보조금 자동 신청",
                    event.getEventId()
            );

            try {

                // 10. 신청 데이터 DB 저장
                appRepository.save(application);

                // 11. 보조금 신청 수 증가
                subsidy.increaseApplicationCount(1);

                applicationCount++;

                log.info(
                        "국가보조금 자동 신청 완료: userId={}, subsidyId={}, eventId={}",
                        user.getId(),
                        subsidy.getId(),
                        event.getEventId()
                );

            } catch (DataIntegrityViolationException e) {

                log.warn(
                        "국가보조금 중복 신청 차단: userId={}, subsidyId={}",
                        user.getId(),
                        subsidy.getId()
                );
            }
        }

        return applicationCount;
    }
}