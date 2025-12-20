package com.save_help.Save_Help.counseling.controller;

import com.save_help.Save_Help.counseling.dto.CounselingNoteResponse;
import com.save_help.Save_Help.counseling.dto.CounselingNoteUpsertRequest;
import com.save_help.Save_Help.counseling.dto.CounselingRequestDto;
import com.save_help.Save_Help.counseling.dto.CounselingResponseDto;
import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.counseling.entity.RecipientType;
import com.save_help.Save_Help.counseling.repository.CounselingNotificationRepository;
import com.save_help.Save_Help.counseling.service.CounselingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counselings")
@RequiredArgsConstructor
@Tag(name = "Counseling API", description = "상담 관리 API")
public class CounselingController {

    private final CounselingService counselingService;
    private final CounselingNotificationRepository notificationRepository;

    // 상담 생성
    @Operation(summary = "상담 생성", description = "새로운 상담을 등록합니다.")
    @PostMapping("/create")
    public ResponseEntity<CounselingResponseDto> create(@RequestBody CounselingRequestDto dto) {
        return ResponseEntity.ok(counselingService.createCounseling(dto));
    }

    // 상담 단건 조회
    @Operation(summary = "상담 단건 조회", description = "상담 ID로 특정 상담을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CounselingResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(counselingService.getCounseling(id));
    }

    // 전체 상담 조회
    @Operation(summary = "전체 상담 조회", description = "등록된 모든 상담을 조회합니다.")
    @GetMapping("/getAll")
    public ResponseEntity<List<CounselingResponseDto>> getAll() {
        return ResponseEntity.ok(counselingService.getAllCounselings());
    }

    // 상담 수정
    @Operation(summary = "상담 수정", description = "상담 내용을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CounselingResponseDto> update(@PathVariable Long id,
                                                        @RequestBody CounselingRequestDto dto) {
        return ResponseEntity.ok(counselingService.updateCounseling(id, dto));
    }

    // 상담 삭제
    @Operation(summary = "상담 삭제", description = "특정 상담을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        counselingService.deleteCounseling(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "상담 SOAP 노트 조회")
    @GetMapping("/soap")
    public ResponseEntity<CounselingNoteResponse> get(@PathVariable Long counselingId) {
        return ResponseEntity.ok(counselingService.get(counselingId));
    }

    @Operation(summary = "상담 SOAP 노트 생성/수정(Upsert)")
    @PutMapping("/soap")
    public ResponseEntity<CounselingNoteResponse> upsert(
            @PathVariable Long counselingId,
            @RequestParam Long authorId,
            @RequestBody CounselingNoteUpsertRequest req
    ) {
        return ResponseEntity.ok(counselingService.upsert(counselingId, authorId, req));
    }


    @Operation(summary = "상담 피드백 작성", description = "종료된 상담에 대해 피드백을 작성합니다")
    @PostMapping("/{counselingId}/feedback")
    public Long create(
            @PathVariable Long counselingId,
            @RequestParam Long userId,
            @RequestBody @Valid CounselingService.CreateCounselingFeedbackRequest req
    ) {
        return counselingService.createFeedback(userId, counselingId, req);
    }

    @Operation(summary = "내 상담 피드백 조회", description = "내가 작성한 상담 피드백을 조회합니다")
    @GetMapping("/{counselingId}/feedback")
    public CounselingService.CounselingFeedbackResponse get(
            @PathVariable Long counselingId,
            @RequestParam Long userId
    ) {
        return counselingService.getMyFeedback(userId, counselingId);
    }

    //조회/읽음 처리
    @Operation(summary = "상담 알림 조회", description = "상담 알림들을 조회합니다")
    @GetMapping("/api/notifications")
    public ResponseEntity<?> list(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                notificationRepository.findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(
                        recipientType, recipientId, PageRequest.of(page, size)
                )
        );
    }

    @Operation(summary = "상담 알림 읽음처리", description = "상담 알림들을 읽음처리합니다")
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> read(@PathVariable Long id) {
        var n = notificationRepository.findById(id).orElseThrow();
        n.setRead(true);
        return ResponseEntity.ok().build();
    }

}
