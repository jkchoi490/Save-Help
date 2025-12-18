package com.save_help.Save_Help.emergency.repository;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmergencyRepository extends JpaRepository<Emergency, Long>, JpaSpecificationExecutor<Emergency> {
    List<Emergency> findByRequester(User requester);

    List<Emergency> findByAssignedHelperIsNullAndResolvedFalse();

    List<Emergency> findByAssignedHelperIsNullAndStatusIn(List<EmergencyStatus> statuses);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Emergency e where e.id = :id")
    Optional<Emergency> findByIdForUpdate(@Param("id") Long id);

}
