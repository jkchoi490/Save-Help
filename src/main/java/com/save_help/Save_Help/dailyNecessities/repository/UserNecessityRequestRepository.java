package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;
import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNecessityRequestRepository extends JpaRepository<UserNecessityRequest, Long> {
    List<UserNecessityRequest> findByUser_Id(Long userId);
    List<UserNecessityRequest> findByStatus(RequestStatus status);

    List<UserNecessityRequest> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);


}