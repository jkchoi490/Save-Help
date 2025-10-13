package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelperAssignmentService {

    private final HelperRepository helperRepository;
    private final EmergencyRepository emergencyRepository;

    @Transactional
    public void assignHelpersAutomatically() {
        //이렇게 하면 아직 헬퍼가 배정되지 않았고(assignedHelper가 null),
        //해결되지 않은(RESOLVED, CANCELLED이 아닌) 응급 요청들
        List<Emergency> activeEmergencies = emergencyRepository.findByAssignedHelperIsNullAndStatusIn(
                List.of(EmergencyStatus.PENDING, EmergencyStatus.REQUESTED, EmergencyStatus.ASSIGNED, EmergencyStatus.IN_PROGRESS)
        );



       // List<Emergency> unassignedEmergencies = emergencyRepository.findByAssignedHelperIsNullAndResolvedFalse();
    /*
        for (Emergency emergency : unassignedEmergencies) {
            List<Helper> candidates = helperRepository.findByAvailableTrueAndSpecialization(emergency.getType());

            if (candidates.isEmpty()) continue;

            // 가장 가까운 Helper 선택 (간단하게 지역 문자열 비교 기반)
            Helper selected = candidates.stream()
                    .min(Comparator.comparing(helper -> distance(helper.getLocation(), emergency.getLocation())))
                    .orElse(null);

            if (selected != null) {
                selected.setAvailable(false);
                emergency.setAssignedHelper(selected);
                emergencyRepository.save(emergency);
            }
        }

*/
    }
    // 거리 계산
    private double distance(String loc1, String loc2) {
        return Math.random(); // 임시
    }
}