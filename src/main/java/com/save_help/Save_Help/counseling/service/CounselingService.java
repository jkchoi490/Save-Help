package com.save_help.Save_Help.counseling.service;

import com.save_help.Save_Help.counseling.dto.CounselingNoteResponse;
import com.save_help.Save_Help.counseling.dto.CounselingNoteUpsertRequest;
import com.save_help.Save_Help.counseling.dto.CounselingRequestDto;
import com.save_help.Save_Help.counseling.dto.CounselingResponseDto;
import com.save_help.Save_Help.counseling.entity.*;
import com.save_help.Save_Help.counseling.repository.CounselingFeedbackRepository;
import com.save_help.Save_Help.counseling.repository.CounselingNoteRepository;
import com.save_help.Save_Help.counseling.repository.CounselingNotificationRepository;
import com.save_help.Save_Help.counseling.repository.CounselingRepository;
import com.save_help.Save_Help.helper.entity.Helper;

import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CounselingService {

    private final CounselingRepository counselingRepository;
    private final UserRepository userRepository;
    private final HelperRepository helperRepository;
    private final CounselingNoteRepository noteRepository;
    private final CounselingFeedbackRepository feedbackRepository;
    private final CounselingNotificationRepository notificationRepository;


    // 상담 등록
    public CounselingResponseDto createCounseling(CounselingRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Helper counselor = helperRepository.findById(dto.getCounselorId())
                .orElseThrow(() -> new EntityNotFoundException("Counselor not found"));

        Counseling counseling = new Counseling();
        counseling.setUser(user);
        counseling.setCounselor(counselor);
        counseling.setStartAt(dto.getStartAt());
        counseling.setEndAt(dto.getEndAt());
        counseling.setStatus(dto.getStatus());
        counseling.setNotes(dto.getNotes());
        counseling.setType(dto.getType());

        Counseling saved = counselingRepository.save(counseling);
        return toDto(saved);
    }

    // 상담 조회 (단건)
    public CounselingResponseDto getCounseling(Long id) {
        Counseling counseling = counselingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Counseling not found"));
        return toDto(counseling);
    }

    // 전체 상담 조회
    public List<CounselingResponseDto> getAllCounselings() {
        return counselingRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 상담 수정
    public CounselingResponseDto updateCounseling(Long id, CounselingRequestDto dto) {
        Counseling counseling = counselingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Counseling not found"));

        if (dto.getStartAt() != null) counseling.setStartAt(dto.getStartAt());
        if (dto.getEndAt() != null) counseling.setEndAt(dto.getEndAt());
        if (dto.getStatus() != null) counseling.setStatus(dto.getStatus());
        if (dto.getNotes() != null) counseling.setNotes(dto.getNotes());
        if (dto.getType() != null) counseling.setType(dto.getType());

        Counseling updated = counselingRepository.save(counseling);
        return toDto(updated);
    }

    // 상담 삭제
    public void deleteCounseling(Long id) {
        counselingRepository.deleteById(id);
    }

    // Entity → DTO 변환
    private CounselingResponseDto toDto(Counseling counseling) {
        return CounselingResponseDto.builder()
                .id(counseling.getId())
                .userId(counseling.getUser().getId())
                .counselorId(counseling.getCounselor().getId())
                .startAt(counseling.getStartAt())
                .endAt(counseling.getEndAt())
                .status(counseling.getStatus())
                .notes(counseling.getNotes())
                .type(counseling.getType())
                .build();
    }



    @Transactional
    public CounselingNoteResponse upsert(Long counselingId, Long authorId, CounselingNoteUpsertRequest req) {
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new EntityNotFoundException("Counseling not found: " + counselingId));

        Helper author = helperRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Helper not found: " + authorId));

        CounselingNote note = noteRepository.findByCounselingId(counselingId)
                .orElseGet(() -> CounselingNote.builder()
                        .counseling(counseling)
                        .author(author)
                        .isFinalized(false)
                        .build());

        // 확정된 노트는 덮어쓰기 금지(원하면 리비전으로 확장)
        if (Boolean.TRUE.equals(note.getIsFinalized())) {
            throw new IllegalStateException("Note already finalized.");
        }

        note.setSubjective(req.getSubjective());
        note.setObjective(req.getObjective());
        note.setAssessment(req.getAssessment());
        note.setPlan(req.getPlan());

        if (Boolean.TRUE.equals(req.getFinalize())) {
            note.setIsFinalized(true);
        }

        CounselingNote saved = noteRepository.save(note);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CounselingNoteResponse get(Long counselingId) {
        CounselingNote note = noteRepository.findByCounselingId(counselingId)
                .orElseThrow(() -> new EntityNotFoundException("Note not found for counseling: " + counselingId));
        return toResponse(note);
    }

    private CounselingNoteResponse toResponse(CounselingNote n) {
        return CounselingNoteResponse.builder()
                .id(n.getId())
                .counselingId(n.getCounseling().getId())
                .subjective(n.getSubjective())
                .objective(n.getObjective())
                .assessment(n.getAssessment())
                .plan(n.getPlan())
                .authorId(n.getAuthor() != null ? n.getAuthor().getId() : null)
                .finalized(n.getIsFinalized())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .build();
    }

    @Transactional
    public Long createFeedback(Long userId, Long counselingId, CreateCounselingFeedbackRequest req) {
        Counseling counseling = counselingRepository.findById(counselingId)
                .orElseThrow(() -> new IllegalArgumentException("상담 정보를 찾을 수 없습니다."));

        //  본인 상담만 작성 가능
        if (counseling.getUser() == null || !counseling.getUser().getId().equals(userId)) {
            throw new IllegalStateException("본인의 상담에만 피드백을 남길 수 있습니다.");
        }

        // 종료된 상담만 작성 가능 (권장: COMPLETED만 허용)
        if (counseling.getStatus() != CounselingStatus.COMPLETED) {
            throw new IllegalStateException("종료된 상담에만 피드백을 남길 수 있습니다.");
        }


        // 중복 작성 방지
        if (feedbackRepository.findByCounselingId(counselingId).isPresent()) {
            throw new IllegalStateException("이미 해당 상담에 대한 피드백이 작성되었습니다.");
        }

        //  평점 검증
        validateRating(req.counselorRating(), "상담사 평점");
        validateRating(req.sessionRating(), "상담 만족도");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (counseling.getCounselor() == null) {
            throw new IllegalStateException("상담사가 배정되지 않아 피드백을 남길 수 없습니다.");
        }

        CounselingFeedback feedback = new CounselingFeedback(
                counseling,
                user,
                counseling.getCounselor(),
                req.counselorRating(),
                req.sessionRating(),
                req.comment(),
                req.issue()
        );

        feedbackRepository.save(feedback);

        // (선택) 상담사 평균 평점 반영 로직을 여기서 처리할 수 있음
        return feedback.getId();
    }

    @Transactional(readOnly = true)
    public CounselingFeedbackResponse getMyFeedback(Long userId, Long counselingId) {
        CounselingFeedback feedback = feedbackRepository.findByCounselingId(counselingId)
                .orElseThrow(() -> new IllegalArgumentException("피드백 정보를 찾을 수 없습니다."));

        if (!feedback.getUser().getId().equals(userId)) {
            throw new IllegalStateException("본인의 피드백만 조회할 수 있습니다.");
        }

        return CounselingFeedbackResponse.from(feedback);
    }

    private void validateRating(Integer rating, String field) {
        if (rating == null) return; // 선택값이면 null 허용
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(field + "은(는) 1~5 사이여야 합니다.");
        }
    }

    // DTO
    public record CreateCounselingFeedbackRequest(
            Integer counselorRating,
            Integer sessionRating,
            String comment,
            CounselingFeedbackIssue issue
    ) {}

    public record CounselingFeedbackResponse(
            Long id,
            Long counselingId,
            Integer counselorRating,
            Integer sessionRating,
            String comment,
            CounselingFeedbackIssue issue
    ) {
        public static CounselingFeedbackResponse from(CounselingFeedback f) {
            return new CounselingFeedbackResponse(
                    f.getId(),
                    f.getCounseling().getId(),
                    f.getCounselorRating(),
                    f.getSessionRating(),
                    f.getComment(),
                    f.getIssue()
            );
        }
    }

    @Transactional
    public void createIfAbsent(
            RecipientType recipientType, Long recipientId,
            NotificationType type, String title, String message,
            String deeplink, String uniqueKey
    ) {
        // 빠른 사전 체크(경합이 있으면 아래 UNIQUE에서 최종 방어)
        if (notificationRepository.existsByUniqueKey(uniqueKey)) return;

        CounselingNotification n = new CounselingNotification();
        n.setRecipientType(recipientType);
        n.setRecipientId(recipientId);
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        n.setDeeplink(deeplink);
        n.setUniqueKey(uniqueKey);

        try {
            notificationRepository.save(n);
        } catch (DataIntegrityViolationException ignore) {
            // UNIQUE 충돌이면 이미 생성된 것 → 무시 (멱등)
        }
    }


}

