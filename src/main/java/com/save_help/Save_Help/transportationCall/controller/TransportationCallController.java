package com.save_help.Save_Help.transportationCall.controller;

import com.save_help.Save_Help.transportationCall.dto.TransportationCallRequestDto;
import com.save_help.Save_Help.transportationCall.dto.TransportationCallResponseDto;
import com.save_help.Save_Help.transportationCall.entity.TransportationCallStatus;
import com.save_help.Save_Help.transportationCall.service.TransportationCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transportation-calls")
@RequiredArgsConstructor
public class TransportationCallController {

    private final TransportationCallService callService;

    //교통 호출 생성 기능 개발
    @PostMapping
    public TransportationCallResponseDto createCall(@RequestBody TransportationCallRequestDto dto) {
        return callService.createCall(dto);
    }

    //특정 사용자의 모든 호출 내역 조회 기능
    @GetMapping("/user/{userId}")
    public List<TransportationCallResponseDto> getCallsByUser(@PathVariable Long userId) {
        return callService.getCallsByUser(userId);
    }

    //호출 상태 변경
    @PatchMapping("/{callId}/status")
    public TransportationCallResponseDto updateStatus(
            @PathVariable Long callId,
            @RequestParam TransportationCallStatus status
    ) {
        return callService.updateStatus(callId, status);
    }
}
