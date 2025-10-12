package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.HelperLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HelperLogRepository extends JpaRepository<HelperLog, Long> {

    List<HelperLog> findByHelperId(Long helperId);

    @Query("SELECT AVG(l.satisfactionScore) FROM HelperLog l WHERE l.helper.id = :helperId")
    Double findAverageScore(@Param("helperId") Long helperId);

    @Query("SELECT COUNT(l) FROM HelperLog l WHERE l.helper.id = :helperId")
    Long countByHelperId(@Param("helperId") Long helperId);
}
