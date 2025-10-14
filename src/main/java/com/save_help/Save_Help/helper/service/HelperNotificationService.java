package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.helper.dto.NotificationRequestDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.helper.util.NotificationSender;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HelperNotificationService {

    private final HelperRepository helperRepository;
    private final EmergencyRepository emergencyRepository;
    private final NotificationSender notificationSender;

    public void sendEmergencyNotification(NotificationRequestDto dto) {
        Helper helper = helperRepository.findById(dto.getHelperId())
                .orElseThrow(() -> new EntityNotFoundException("해당 Helper를 찾을 수 없습니다."));

        Emergency emergency = null;
        if (dto.getEmergencyId() != null) {
            emergency = emergencyRepository.findById(dto.getEmergencyId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 Emergency를 찾을 수 없습니다."));
        }

        String message = dto.getMessage();
        if (emergency != null) {
            message += "\n\n🚨 긴급상황: " + emergency.getTitle() +
                    "\n위치: " + emergency.getLocation() +
                    "\n긴급도: " + emergency.getSeverity();
        }

        // 문자 알림
        if (dto.isViaSms() && helper.getPhoneNumber() != null) {
            notificationSender.sendSms(helper.getPhoneNumber(), message);
        }

        // 푸시 알림
        if (dto.isViaPush()) {
            notificationSender.sendPush(dto.getTitle(), message, helper.getId());
        }
    }
}