package com.save_help.Save_Help.nationalSubsidy.repository;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidySubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NationalSubsidySubscriptionRepository
        extends JpaRepository<NationalSubsidySubscription, Long> {

    List<NationalSubsidySubscription> findByUserIdAndActiveTrue(Long userId);
}
