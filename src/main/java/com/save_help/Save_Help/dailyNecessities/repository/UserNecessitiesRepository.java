package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNecessitiesRepository extends JpaRepository<UserNecessityRequest, Long> {
    List<UserNecessityRequest> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
