package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.helper.dto.AdminNoticeRequestDto;
import com.save_help.Save_Help.helper.dto.NotificationRequestDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperNotification;
import com.save_help.Save_Help.helper.entity.NotificationType;
import com.save_help.Save_Help.helper.repository.HelperNotificationRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.helper.util.NotificationSender;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HelperNotificationService {

    private final HelperRepository helperRepository;
    private final EmergencyRepository emergencyRepository;
    private final NotificationSender notificationSender;

    private final HelperNotificationRepository notificationRepository;
    private final CommunityCenterRepository centerRepository;

    //
    // private final TwilioService twilioService;
    // private final PushService pushService;


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
            message += "\n\n 긴급상황: " + emergency.getTitle() +
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

    public void notifyAdmins(String message) {
        //계속 개발 중...
    }

    public void notifyHelper(Long id, String s) {

    }


    public void sendAdminNotice(AdminNoticeRequestDto dto) {

        List<Helper> targets;

        // 1) 전체 헬퍼에게 공지
        if (dto.isSendToAll()) {
            targets = helperRepository.findAll();
        }
        // 2) 역할 기반 공지
        else if (dto.getRole() != null) {
            targets = helperRepository.findByRole(dto.getRole());
        }
        // 3) 센터 기반 공지
        else if (dto.getCenterId() != null) {
            CommunityCenter center = centerRepository.findById(dto.getCenterId())
                    .orElseThrow(() -> new IllegalArgumentException("Center not found"));

            targets = helperRepository.findByCommunityCenter(center);
        }
        else {
            throw new IllegalArgumentException("공지 발송 대상이 설정되지 않았습니다.");
        }

        // 4) 일괄 공지 생성
        for (Helper helper : targets) {
            HelperNotification notice = new HelperNotification();
            notice.setHelper(helper);
            notice.setType(NotificationType.ADMIN_NOTICE);
            notice.setMessage("[" + dto.getTitle() + "] " + dto.getMessage());
            notificationRepository.save(notice);
        }
    }
}