package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import com.save_help.Save_Help.dailyNecessities.repository.UserNecessitiesRepository;
import com.save_help.Save_Help.dailyNecessities.repository.UserNecessityRequestRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserNecessityRequestService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final UserNecessityRequestRepository requestRepository;
    private final UserRepository userRepository;

    public UserNecessityRequestService(DailyNecessitiesRepository necessitiesRepository,
                                       UserNecessityRequestRepository requestRepository,
                                       UserRepository userRepository) {
        this.necessitiesRepository = necessitiesRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    // 사용자 자동 신청 생성
    public UserNecessityRequest createRequest(Long userId, Long itemId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        DailyNecessities item = necessitiesRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("생필품을 찾을 수 없습니다."));

        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");

        UserNecessityRequest request = new UserNecessityRequest(item, user, quantity);
        return requestRepository.save(request);
    }

    // 사용자별 신청 내역 조회
    public List<UserNecessityRequest> getRequestsByUser(Long userId) {
        return requestRepository.findByUser_Id(userId);
    }

    // 신청 상태 변경 (관리자용)
    public void updateRequestStatus(Long requestId, UserNecessityRequest.RequestStatus status) {
        UserNecessityRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("신청 내역을 찾을 수 없습니다."));
        request.setStatus(status);
        requestRepository.save(request);
    }

    // (선택) 재고가 충분하면 자동 승인
    public void autoApproveRequest(UserNecessityRequest request) {
        DailyNecessities item = request.getItem();
        if (item.getStock() >= request.getQuantity()) {
            item.setStock(item.getStock() - request.getQuantity());
            request.setStatus(UserNecessityRequest.RequestStatus.APPROVED);
            necessitiesRepository.save(item);
            requestRepository.save(request);
        }
    }
}
