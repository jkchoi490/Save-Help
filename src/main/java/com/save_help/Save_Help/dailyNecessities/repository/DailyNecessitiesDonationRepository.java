package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDonation;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDonation.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesDonationRepository extends JpaRepository<DailyNecessitiesDonation, Long> {

    List<DailyNecessitiesDonation> findByDonor_Id(Long userId);
    List<DailyNecessitiesDonation> findByStatus(Status status);
}
