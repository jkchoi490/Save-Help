package com.save_help.Save_Help.emergency.repository;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import com.save_help.Save_Help.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    List<Emergency> findByRequester(User requester);

    List<Emergency> findByAssignedHelperIsNullAndResolvedFalse();

    List<Emergency> findByAssignedHelperIsNullAndStatusIn(List<EmergencyStatus> statuses);
}
