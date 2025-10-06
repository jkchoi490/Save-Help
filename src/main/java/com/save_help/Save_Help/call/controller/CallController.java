package com.save_help.Save_Help.call.controller;

import com.save_help.Save_Help.call.dto.CallRequestDto;
import com.save_help.Save_Help.call.dto.CallResponseDto;
import com.save_help.Save_Help.call.dto.CallStatusUpdateDto;
import com.save_help.Save_Help.call.service.CallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;

    // 통화 요청
    @PostMapping
    public ResponseEntity<CallResponseDto> requestCall(@RequestBody CallRequestDto dto) {
        return ResponseEntity.ok(callService.requestCall(dto));
    }

    // 통화 상태 업데이트 (수락, 거절, 종료)
    @PatchMapping("/{callId}/status")
    public ResponseEntity<CallResponseDto> updateStatus(
            @PathVariable Long callId,
            @RequestBody CallStatusUpdateDto dto
    ) {
        return ResponseEntity.ok(callService.updateStatus(callId, dto));
    }

    // 통화 정보 조회
    @GetMapping("/{callId}")
    public ResponseEntity<CallResponseDto> getCall(@PathVariable Long callId) {
        return ResponseEntity.ok(callService.getCall(callId));
    }
}
