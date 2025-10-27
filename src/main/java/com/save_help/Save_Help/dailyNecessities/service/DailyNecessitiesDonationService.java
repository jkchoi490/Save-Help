package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDonation;
import com.save_help.Save_Help.dailyNecessities.entity.DonationPointHistory;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesDonationPointHistoryRepository;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesDonationRepository;
import com.save_help.Save_Help.dailyNecessities.service.DailyNecessitiesService;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DailyNecessitiesDonationService {

    private final DailyNecessitiesDonationRepository donationRepository;
    private final CommunityCenterRepository centerRepository;
    private final UserRepository userRepository;
    private final DailyNecessitiesService necessitiesService;
    private final DailyNecessitiesDonationPointHistoryRepository historyRepository;

    public DailyNecessitiesDonationService(DailyNecessitiesDonationRepository donationRepository,
                                           CommunityCenterRepository centerRepository,
                                           UserRepository userRepository,
                                           DailyNecessitiesService necessitiesService, DailyNecessitiesDonationPointHistoryRepository historyRepository) {
        this.donationRepository = donationRepository;
        this.centerRepository = centerRepository;
        this.userRepository = userRepository;
        this.necessitiesService = necessitiesService;
        this.historyRepository = historyRepository;
    }

    // 사용자 기부 요청
    public DailyNecessitiesDonation donate(Long userId, Long centerId, String name,
                                           NecessityCategory category, String unit, Integer quantity) {
        User donor = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        CommunityCenter center = centerRepository.findById(centerId)
                .orElseThrow(() -> new EntityNotFoundException("센터를 찾을 수 없습니다."));

        DailyNecessitiesDonation donation = new DailyNecessitiesDonation(name, category, unit, quantity, center, donor);
        return donationRepository.save(donation);
    }

    // 🔹 관리자 승인
    public DailyNecessitiesDonation approve(Long donationId) {
        DailyNecessitiesDonation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new EntityNotFoundException("기부 요청을 찾을 수 없습니다."));
        donation.approve();

        // 승인 시 재고에 추가

        DailyNecessities existingItem = necessitiesService
                .findOrCreateItem(donation.getName(), donation.getCategory(), donation.getUnit(), donation.getCenter());
        existingItem.setStock(existingItem.getStock() + donation.getQuantity());
        necessitiesService.save(existingItem);

        return donation;
    }

    // 관리자 거부
    public DailyNecessitiesDonation reject(Long donationId) {
        DailyNecessitiesDonation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new EntityNotFoundException("기부 요청을 찾을 수 없습니다."));
        donation.reject();
        return donation;
    }

    // 사용자 본인 기부 내역 조회
    public List<DailyNecessitiesDonation> getMyDonations(Long userId) {
        return donationRepository.findByDonor_Id(userId);
    }

    // 관리자 승인 대기 목록 조회
    public List<DailyNecessitiesDonation> getPendingDonations() {
        return donationRepository.findByStatus(DailyNecessitiesDonation.Status.PENDING);
    }

    // 포인트 계산 정책
    public int calculatePoints(DailyNecessitiesDonation donation) {
        if (donation.getQuantity() == null) return 0;
        int points = donation.getQuantity() * 10; // 수량 1개당 10점
        return points;
    }

    @Transactional
    public void accruePoints(DailyNecessitiesDonation donation) {
        User donor = donation.getDonor();
        int points = calculatePoints(donation);

        // 누적 포인트 업데이트
        donor.setTotalDonationPoints(donor.getTotalDonationPoints() + points);
        userRepository.save(donor);

        // 포인트 히스토리 기록
        DonationPointHistory history = DonationPointHistory.builder()
                .donor(donor)
                .donation(donation)
                .points(points)
                .createdAt(LocalDateTime.now())
                .build();
        historyRepository.save(history);
    }
}
