package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperLog;
import com.save_help.Save_Help.helper.repository.HelperLogRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HelperLogService {

    private final HelperLogRepository logRepository;
    private final HelperRepository helperRepository;
    private final EmergencyRepository emergencyRepository;

    public HelperLog createLog(Long helperId, Long emergencyId, String action, String result, int score) {
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new EntityNotFoundException("Helper not found"));
        Emergency emergency = emergencyRepository.findById(emergencyId)
                .orElseThrow(() -> new EntityNotFoundException("Emergency not found"));

        HelperLog log = new HelperLog();
        log.setHelper(helper);
        log.setEmergency(emergency);
        log.setStartTime(LocalDateTime.now());
        log.setAction(action);
        log.setResult(result);
        log.setSatisfactionScore(score);
        log.setEndTime(LocalDateTime.now().plusMinutes(30)); // 예시: 처리 시간
        return logRepository.save(log);
    }

    public double getAverageScore(Long helperId) {
        return Optional.ofNullable(logRepository.findAverageScore(helperId)).orElse(0.0);
    }

    public long getTotalHandledCases(Long helperId) {
        return logRepository.countByHelperId(helperId);
    }

    public List<HelperLog> getLogsByHelper(Long helperId) {
        return logRepository.findByHelperId(helperId);
    }
}
