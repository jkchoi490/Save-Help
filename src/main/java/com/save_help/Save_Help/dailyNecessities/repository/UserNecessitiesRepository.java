package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;

import java.util.List;

public interface UserNecessitiesRepository {
    List<UserNecessityRequest> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
