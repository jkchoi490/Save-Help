package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.helper.dto.HelperReviewRequestDto;
import com.save_help.Save_Help.helper.dto.HelperReviewResponseDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperReview;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.helper.repository.HelperReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelperReviewService {

    private final HelperRepository helperRepository;
    private final EmergencyRepository emergencyRepository;
    private final HelperReviewRepository reviewRepository;

    @Transactional
    public HelperReviewResponseDto createReview(HelperReviewRequestDto dto) {
        Helper helper = helperRepository.findById(dto.getHelperId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Helper가 존재하지 않습니다."));
        Emergency emergency = emergencyRepository.findById(dto.getEmergencyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 긴급 상황이 존재하지 않습니다."));

        HelperReview review = HelperReview.builder()
                .helper(helper)
                .emergency(emergency)
                .rating(dto.getRating())
                .feedback(dto.getFeedback())
                .build();

        reviewRepository.save(review);

        updateHelperTrustScore(helper.getId());

        return HelperReviewResponseDto.builder()
                .reviewId(review.getId())
                .helperId(helper.getId())
                .rating(review.getRating())
                .feedback(review.getFeedback())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public List<HelperReviewResponseDto> getReviewsByHelper(Long helperId) {
        return reviewRepository.findByHelperId(helperId).stream()
                .map(r -> HelperReviewResponseDto.builder()
                        .reviewId(r.getId())
                        .helperId(r.getHelper().getId())
                        .rating(r.getRating())
                        .feedback(r.getFeedback())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private void updateHelperTrustScore(Long helperId) {
        Double avg = reviewRepository.findAverageRatingByHelperId(helperId);
        if (avg == null) return;

        Helper helper = helperRepository.findById(helperId).orElseThrow();
        helper.setTrustScore(avg);

        // 낮은 평점(예: 2.5 미만)일 경우 상태 조정
        if (avg < 2.5) {
            helper.setActivityStatus(com.save_help.Save_Help.helper.entity.HelperActivityStatus.REVIEW_REQUIRED);
            log.warn("헬퍼 {}의 신뢰도 낮음. 상태 REVIEW_REQUIRED로 변경", helperId);
        }

        helperRepository.save(helper);
    }
}
