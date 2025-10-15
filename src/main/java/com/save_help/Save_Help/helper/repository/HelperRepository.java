package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HelperRepository extends JpaRepository<Helper, Long> {

    // 역할별 조회
    List<Helper> findByRole(HelperRole role);

    // available 여부로 조회
    List<Helper> findByAvailableTrue();

    // 역할 + available=true 조건 조회
    List<Helper> findByRoleAndAvailableTrue(HelperRole role);

    //List<Helper> findByCenterId(Long centerId);
    List<Helper> findByHospitalId(Long hospitalId);

    @Query("SELECT h FROM Helper h WHERE h.communityCenter.id = :centerId")
    List<Helper> findByCommunityCenter_Id(Long centerId);


}