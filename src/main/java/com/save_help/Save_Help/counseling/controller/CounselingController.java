package com.save_help.Save_Help.counseling.controller;

import com.save_help.Save_Help.counseling.dto.CounselingRequestDto;
import com.save_help.Save_Help.counseling.dto.CounselingResponseDto;
import com.save_help.Save_Help.counseling.service.CounselingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counselings")
@RequiredArgsConstructor
public class CounselingController {

    private final CounselingService counselingService;

    // 상담 생성
    @PostMapping
    public ResponseEntity<CounselingResponseDto> create(@RequestBody CounselingRequestDto dto) {
        return ResponseEntity.ok(counselingService.createCounseling(dto));
    }

    // 상담 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<CounselingResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(counselingService.getCounseling(id));
    }

    // 전체 상담 조회
    @GetMapping
    public ResponseEntity<List<CounselingResponseDto>> getAll() {
        return ResponseEntity.ok(counselingService.getAllCounselings());
    }

    // 상담 수정
    @PutMapping("/{id}")
    public ResponseEntity<CounselingResponseDto> update(@PathVariable Long id,
                                                        @RequestBody CounselingRequestDto dto) {
        return ResponseEntity.ok(counselingService.updateCounseling(id, dto));
    }

    // 상담 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        counselingService.deleteCounseling(id);
        return ResponseEntity.noContent().build();
    }
}
