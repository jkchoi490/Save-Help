package com.save_help.Save_Help.nationalSubsidy.service;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidyApplication;
import com.save_help.Save_Help.nationalSubsidy.repository.NationalSubsidyApplicationRepository;
import com.save_help.Save_Help.nationalSubsidy.repository.NationalSubsidyRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NationalSubsidyAutoApplyService {

    private final NationalSubsidyRepository subsidyRepository;
    private final UserRepository userRepository;
    private final NationalSubsidyApplicationRepository appRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void applyForSubsidyToUsers(Long subsidyId) {
        NationalSubsidy subsidy = subsidyRepository.findById(subsidyId).orElseThrow();

        if (!isRunnableSubsidy(subsidy)) return;

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

            for (User user : users.getContent()) {
                tryInsertApplication(user, subsidy, "Matched by subsidy event");
            }

            if (!users.hasNext()) break;
            page++;
        }
    }

    @Transactional
    public void applyForUserToSubsidies(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        int page = 0;
        int size = 500;
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

            for (NationalSubsidy subsidy : subsidies.getContent()) {
                if (!isRunnableSubsidy(subsidy)) continue;
                tryInsertApplication(user, subsidy, "Matched by user event");
            }

            if (!subsidies.hasNext()) break;
            page++;
        }
    }

    private boolean isRunnableSubsidy(NationalSubsidy s) {
        if (!s.isActive()) return false;

        LocalDate today = LocalDate.now();
        if (s.getStartDate() != null && s.getStartDate().isAfter(today)) return false;
        if (s.getEndDate() != null && s.getEndDate().isBefore(today)) return false;

        return true;
    }

    private void tryInsertApplication(User user, NationalSubsidy subsidy, String reason) {
        try {
            NationalSubsidyApplication saved = appRepository.save(
                    NationalSubsidyApplication.builder()
                            .user(user)
                            .subsidy(subsidy)
                            .status(NationalSubsidyApplication.Status.APPLIED)
                            .reason(reason)
                            // .active(true)  // NationalSubsidyApplication에 active 필드를 추가했다면 사용
                            .build()
            );


            publisher.publishEvent(new ApplicationCreatedInternalEvent(
                    saved.getId(),
                    user.getId(),
                    subsidy.getId()
            ));

        } catch (DataIntegrityViolationException dup) {
            // (user_id, subsidy_id) 유니크 충돌 -> 이미 존재 -> 멱등 OK
        }
    }
}
