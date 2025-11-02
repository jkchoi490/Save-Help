package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.dto.HelperFeedbackDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperFeedback;
import com.save_help.Save_Help.helper.repository.HelperFeedbackRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HelperFeedbackService {

    private final HelperRepository helperRepository;
    private final HelperFeedbackRepository feedbackRepository;

    // 1️. 헬퍼 피드백 등록
    @Transactional
    public HelperFeedbackDto createFeedback(Long helperId, String title, String content) {
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new IllegalArgumentException("해당 헬퍼를 찾을 수 없습니다."));

        HelperFeedback feedback = HelperFeedback.builder()
                .helper(helper)
                .title(title)
                .content(content)
                .status(HelperFeedback.FeedbackStatus.PENDING)
                .build();

        feedbackRepository.save(feedback);
        return HelperFeedbackDto.fromEntity(feedback);
    }

    // 2️. 헬퍼의 피드백 목록 조회
    @Transactional(readOnly = true)
    public List<HelperFeedbackDto> getFeedbacksByHelper(Long helperId) {
        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new IllegalArgumentException("해당 헬퍼를 찾을 수 없습니다."));
        return feedbackRepository.findByHelper(helper)
                .stream()
                .map(HelperFeedbackDto::fromEntity)
                .toList();
    }

    // 3️. 관리자 전체 피드백 조회
    @Transactional(readOnly = true)
    public List<HelperFeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll()
                .stream()
                .map(HelperFeedbackDto::fromEntity)
                .toList();
    }

    // 4️. 관리자 피드백 답변/상태 변경
    @Transactional
    public HelperFeedbackDto respondToFeedback(Long feedbackId, String response, HelperFeedback.FeedbackStatus status) {
        HelperFeedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드백을 찾을 수 없습니다."));
        feedback.respond(response, status);
        return HelperFeedbackDto.fromEntity(feedback);
    }
}
