package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesReviewDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesReview;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesReviewRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyNecessitiesReviewService {

    private final DailyNecessitiesReviewRepository reviewRepository;
    private final DailyNecessitiesRepository itemRepository;
    private final UserRepository userRepository;


    @Transactional
    public DailyNecessitiesReviewDto createReview(Long userId, Long itemId, int rating, String comment) {
        User user = userRepository.findById(userId).orElseThrow();
        DailyNecessities item = itemRepository.findById(itemId).orElseThrow();

        DailyNecessitiesReview review = new DailyNecessitiesReview();
        review.setReviewer(user);
        review.setItem(item);
        review.setRating(rating);
        review.setComment(comment);

        reviewRepository.save(review);

        // 평균 평점 업데이트
        double avg = reviewRepository.findByItem(item)
                .stream().mapToInt(DailyNecessitiesReview::getRating).average().orElse(7.0);
        item.setAverageRating(avg);


        return DailyNecessitiesReviewDto.fromEntity(review);
    }

    public List<DailyNecessitiesReviewDto> getReviewsByItem(Long itemId) {
        DailyNecessities item = itemRepository.findById(itemId).orElseThrow();
        return reviewRepository.findByItem(item)
                .stream()
                .map(DailyNecessitiesReviewDto::fromEntity)
                .toList();
    }
}
