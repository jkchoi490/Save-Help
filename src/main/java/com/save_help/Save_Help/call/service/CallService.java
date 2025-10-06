package com.save_help.Save_Help.call.service;

import com.save_help.Save_Help.call.dto.CallRequestDto;
import com.save_help.Save_Help.call.dto.CallResponseDto;
import com.save_help.Save_Help.call.dto.CallStatusUpdateDto;
import com.save_help.Save_Help.call.entity.Call;
import com.save_help.Save_Help.call.entity.CallStatus;
import com.save_help.Save_Help.call.repository.CallRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CallService {

    private final CallRepository callRepository;
    private final UserRepository userRepository;

    // 통화 요청 생성
    public CallResponseDto requestCall(CallRequestDto requestDto) {
        User caller = userRepository.findById(requestDto.getCallerId())
                .orElseThrow(() -> new IllegalArgumentException("Caller not found"));
        User callee = userRepository.findById(requestDto.getCalleeId())
                .orElseThrow(() -> new IllegalArgumentException("Callee not found"));

        Call call = new Call();
        call.setCaller(caller);
        call.setCallee(callee);
        call.setStatus(CallStatus.REQUESTED);
        call.setStartedAt(LocalDateTime.now());

        Call saved = callRepository.save(call);

        return new CallResponseDto(
                saved.getId(),
                caller.getId(),
                callee.getId(),
                saved.getStatus(),
                saved.getStartedAt(),
                saved.getEndedAt()
        );
    }

    // 통화 상태 변경 (수락, 거절, 종료)
    public CallResponseDto updateStatus(Long callId, CallStatusUpdateDto statusDto) {
        Call call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        call.setStatus(statusDto.getStatus());
        if (statusDto.getStatus() == CallStatus.ENDED) {
            call.setEndedAt(LocalDateTime.now());
        }

        Call updated = callRepository.save(call);

        return new CallResponseDto(
                updated.getId(),
                updated.getCaller().getId(),
                updated.getCallee().getId(),
                updated.getStatus(),
                updated.getStartedAt(),
                updated.getEndedAt()
        );
    }

    // 통화 정보 조회
    @Transactional(readOnly = true)
    public CallResponseDto getCall(Long callId) {
        Call call = callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found"));

        return new CallResponseDto(
                call.getId(),
                call.getCaller().getId(),
                call.getCallee().getId(),
                call.getStatus(),
                call.getStartedAt(),
                call.getEndedAt()
        );
    }
}
