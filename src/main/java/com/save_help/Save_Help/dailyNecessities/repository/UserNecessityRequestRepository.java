package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;
import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNecessityRequestRepository extends JpaRepository<UserNecessityRequest, Long> {
    List<UserNecessityRequest> findByUser_Id(Long userId);
    List<UserNecessityRequest> findByStatus(RequestStatus status);

    List<UserNecessityRequest> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    //@Query("SELECT COUNT(r) FROM UserNecessityRequest r WHERE r.item.center.id = :centerId AND r.status = 'PENDING'")
    //Long countPendingRequestsByCenter(@Param("centerId") Long centerId);
    Long countByItem_ProvidedBy_IdAndStatus(Long centerId, RequestStatus status);

}