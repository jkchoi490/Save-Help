package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperSchedule;
import com.save_help.Save_Help.helper.entity.ScheduleStatus;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.helper.repository.HelperScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelperScheduleService {

    private final HelperRepository helperRepository;
    private final HelperScheduleRepository scheduleRepository;

    public HelperSchedule createSchedule(Long helperId, LocalDateTime start, LocalDateTime end, String note) {
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new EntityNotFoundException("Helper not found"));

        HelperSchedule schedule = new HelperSchedule();
        schedule.setHelper(helper);
        schedule.setStartTime(start);
        schedule.setEndTime(end);
        schedule.setStatus(ScheduleStatus.PLANNED);
        schedule.setNote(note);
        schedule.setLocation(helper.getCommunityCenter() != null
                ? helper.getCommunityCenter().getName()
                : helper.getHospital() != null ? helper.getHospital().getName() : "미지정");

        return scheduleRepository.save(schedule);
    }

    public List<HelperSchedule> getActiveSchedules() {
        return scheduleRepository.findCurrentlyActive(LocalDateTime.now());
    }

    public List<HelperSchedule> getSchedulesByHelper(Long helperId) {
        return scheduleRepository.findByHelperId(helperId);
    }
    /**
     * 출근 (startTime 기록)
     */
    public HelperSchedule clockIn(Long helperId) {
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new EntityNotFoundException("Helper not found"));

        HelperSchedule schedule = new HelperSchedule();
        schedule.setHelper(helper);
        schedule.setStartTime(LocalDateTime.now());
        schedule.setStatus(ScheduleStatus.IN_PROGRESS);
        schedule.setLocation(helper.getCommunityCenter() != null ?
                helper.getCommunityCenter().getName() :
                (helper.getHospital() != null ? helper.getHospital().getName() : "미지정"));

        return scheduleRepository.save(schedule);
    }

    /**
     * 퇴근 (endTime 기록 및 총 근무시간 계산)
     */
    public HelperSchedule clockOut(Long helperId) {
        HelperSchedule schedule = scheduleRepository.findTopByHelperIdOrderByStartTimeDesc(helperId);

        if (schedule == null || schedule.getStatus() != ScheduleStatus.IN_PROGRESS) {
            throw new IllegalStateException("현재 진행 중인 근무가 없습니다.");
        }

        schedule.setEndTime(LocalDateTime.now());
        schedule.setStatus(ScheduleStatus.COMPLETED);
        return scheduleRepository.save(schedule);
    }

    /**
     * Helper의 전체 근무 기록 조회
     */
    public List<HelperSchedule> getSchedules(Long helperId) {
        return scheduleRepository.findByHelperId(helperId);
    }

    /**
     * Helper의 총 근무시간 계산 (분 단위)
     */
    public long getTotalWorkMinutes(Long helperId) {
        List<HelperSchedule> schedules = scheduleRepository.findByHelperId(helperId);

        return schedules.stream()
                .filter(s -> s.getStartTime() != null && s.getEndTime() != null)
                .mapToLong(s -> Duration.between(s.getStartTime(), s.getEndTime()).toMinutes())
                .sum();
    }

    /**
     * Helper의 총 근무시간을 "X시간 Y분" 형식으로 반환
     */
    public String getTotalWorkSummary(Long helperId) {
        long totalMinutes = getTotalWorkMinutes(helperId);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return hours + "시간 " + minutes + "분";
    }

}
