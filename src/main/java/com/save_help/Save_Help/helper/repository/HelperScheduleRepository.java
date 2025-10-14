package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.HelperSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public interface HelperScheduleRepository extends JpaRepository<HelperSchedule, Long> {

    List<HelperSchedule> findByHelperId(Long helperId);

    @Query("SELECT s FROM HelperSchedule s WHERE :now BETWEEN s.startTime AND s.endTime")
    List<HelperSchedule> findCurrentlyActive(@Param("now") LocalDateTime now);

    @Query("SELECT s FROM HelperSchedule s WHERE s.startTime BETWEEN :start AND :end")
    List<HelperSchedule> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<HelperSchedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    HelperSchedule findTopByHelperIdOrderByStartTimeDesc(Long helperId);
}
