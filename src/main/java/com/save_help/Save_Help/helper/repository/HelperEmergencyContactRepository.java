package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.HelperEmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelperEmergencyContactRepository extends JpaRepository<HelperEmergencyContact, Long> {
    List<HelperEmergencyContact> findByHelperId(Long helperId);

    List<HelperEmergencyContact> findByUserId(Long userId);
}
