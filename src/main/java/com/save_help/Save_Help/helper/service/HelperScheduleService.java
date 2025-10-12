package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperSchedule;
import com.save_help.Save_Help.helper.entity.ScheduleStatus;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.helper.repository.HelperScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        schedule.setLocation(helper.getCenter() != null
                ? helper.getCenter().getName()
                : helper.getHospital() != null ? helper.getHospital().getName() : "미지정");

        return scheduleRepository.save(schedule);
    }

    public List<HelperSchedule> getActiveSchedules() {
        return scheduleRepository.findCurrentlyActive(LocalDateTime.now());
    }

    public List<HelperSchedule> getSchedulesByHelper(Long helperId) {
        return scheduleRepository.findByHelperId(helperId);
    }
}
