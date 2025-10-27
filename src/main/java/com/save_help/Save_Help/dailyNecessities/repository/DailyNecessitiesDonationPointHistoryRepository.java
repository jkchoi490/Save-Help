package com.save_help.Save_Help.dailyNecessities.repository;
import com.save_help.Save_Help.dailyNecessities.entity.DonationPointHistory;
import com.save_help.Save_Help.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesDonationPointHistoryRepository extends JpaRepository<DonationPointHistory, Long> {
    List<DonationPointHistory> findByDonor(User donor);
}