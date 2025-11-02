package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelperFeedbackRepository extends JpaRepository<HelperFeedback, Long> {
    List<HelperFeedback> findByHelper(Helper helper);
}
