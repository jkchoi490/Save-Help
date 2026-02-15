package com.save_help.Save_Help.nationalSubsidy.kafka.consumer;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyApplication;
import com.save_help.Save_Help.nationalSubsidy.kafka.*;
import com.save_help.Save_Help.nationalSubsidy.repository.NationalSubsidyRepository;
import com.save_help.Save_Help.nationalSubsidy.repository.SubsidyApplicationRepository;
import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyEvaluator;
import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyService;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoApplyConsumers {

    private final NationalSubsidyService autoApplyService;
    private final NationalSubsidyRepository nationalSubsidyRepository;
    private final SubsidyApplicationRepository applicationRepository;
    private final NationalSubsidyEvaluator evaluator;
    private final UserRepository userRepository;

    @KafkaListener(topics = KafkaTopics.SUBSIDY_CREATED, groupId = "subsidy-auto-apply")
    public void onSubsidyCreated(SubsidyCreatedEvent event, Acknowledgment ack) {
        autoApplyService.applyForSubsidyToUsers(event.subsidyId());
        ack.acknowledge();
    }

    @KafkaListener(topics = KafkaTopics.USER_CREATED, groupId = "user-auto-apply")
    public void onUserCreated(UserCreatedEvent event, Acknowledgment ack) {
        autoApplyService.applyForUserToSubsidies(event.userId());
        ack.acknowledge();
    }

    @KafkaListener(topics = KafkaTopics.USER_ELIGIBILITY_UPDATED, groupId = "user-auto-apply")
    public void onUserEligibilityUpdated(UserEligibilityUpdatedEvent event, Acknowledgment ack) {
        autoApplyService.applyForUserToSubsidies(event.userId());
        ack.acknowledge();
    }


    @KafkaListener(
            topics = "user-eligibility-changed",
            groupId = "subsidy-auto-apply"
    )
    @Transactional
    public void onUserEligibilityChanged(RequirementsNationalSubsidy event) {

        User user = userRepository.findById(event.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + event.userId()));

        LocalDate today = LocalDate.now();

        List<NationalSubsidy> candidates =
                nationalSubsidyRepository.findOpenForApplication();

        for (NationalSubsidy s : candidates) {

            if (!evaluator.isApplyNationalSubsidy(user, s, today)) continue;


            if (applicationRepository.existsByUserIdAndSubsidyId(user.getId(), s.getId())) continue;

            try {
                applicationRepository.save(
                        SubsidyApplication.builder()
                                .user(user)
                                .subsidy(s)
                                .status(ApplicationStatus.APPLIED)
                                .build()
                );

            } catch (DataIntegrityViolationException dup) {

            }
        }
    }
}