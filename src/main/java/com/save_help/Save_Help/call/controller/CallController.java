package com.save_help.Save_Help.call.controller;

import com.save_help.Save_Help.call.dto.CallRequestDto;
import com.save_help.Save_Help.call.dto.CallResponseDto;
import com.save_help.Save_Help.call.dto.CallStatusUpdateDto;
import com.save_help.Save_Help.call.entity.Call;
import com.save_help.Save_Help.call.entity.CallStatus;
import com.save_help.Save_Help.call.repository.CallRepository;
import com.save_help.Save_Help.call.service.CallService;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static com.save_help.Save_Help.call.entity.CallStatus.*;

@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;
    private final CallRepository callRepository;

    @Value("${twilio.account.sid}") private String accountSid;
    @Value("${twilio.apiKey.sid}") private String apiKeySid;
    @Value("${twilio.apiKey.secret}") private String apiKeySecret;
    @Value("${twilio.twimlApp.sid}") private String twimlAppSid;

    // 통화 요청
    @Operation(summary = "통화 요청", description = "통화를 요청합니다")
    @PostMapping
    public ResponseEntity<CallResponseDto> requestCall(@RequestBody CallRequestDto dto) {
        return ResponseEntity.ok(callService.requestCall(dto));
    }

    // 통화 상태 업데이트 (수락, 거절, 종료)
    @Operation(summary = "통화 상태 업데이트", description = "통화 상태를 업데이트 합니다")
    @PatchMapping("/{callId}/status")
    public ResponseEntity<CallResponseDto> updateStatus(
            @PathVariable Long callId,
            @RequestBody CallStatusUpdateDto dto
    ) {
        return ResponseEntity.ok(callService.updateStatus(callId, dto));
    }

    // 통화 정보 조회
    @Operation(summary = "통화 정보 조회", description = "통화 정보를 조회합니다")
    @GetMapping("/{callId}")
    public ResponseEntity<CallResponseDto> getCall(@PathVariable Long callId) {
        return ResponseEntity.ok(callService.getCall(callId));
    }

    @Operation(summary = "Twilio 음성 통화 토큰 발급", description = "Twilio 음성 통화를 위한 Access Token을 발급합니다")
    @GetMapping("/twilio/voice/token")
    public Map<String, String> token(@RequestParam String identity) {
        VoiceGrant grant = new VoiceGrant();
        grant.setOutgoingApplicationSid(twimlAppSid);
        grant.setIncomingAllow(false); // 브라우저와 휴대전화 간  “발신만”이면 false로 시작

        AccessToken token = new AccessToken.Builder(accountSid, apiKeySid, apiKeySecret)
                .identity(identity)
                .grant(grant)
                .build();

        return Map.of("token", token.toJwt(), "identity", identity);
    }

    @Operation(summary = "TwiML 생성", description = "Twilio가 호출하는 통화 연결용 TwiML을 반환합니다")
    @PostMapping(value = "/twiml", produces = MediaType.APPLICATION_XML_VALUE)
    public String twiml(
            @RequestParam(name = "to") String toNumber,     // E.164 (+82...)
            @RequestParam(name = "callId") Long callId
    ) {
        Call call = callRepository.findById(callId)
                .orElseThrow(() -> new EntityNotFoundException("Call not found: " + callId));

        // 통화 시작 처리(원하는 시점에 맞춰 조정 가능)
        call.setStatus(ACCEPTED);
        call.setStartedAt(LocalDateTime.now());
        callRepository.save(call);

        Dial dial = new Dial.Builder()
                .number(toNumber)
                .build();


        VoiceResponse response = new VoiceResponse.Builder()
                .dial(dial)
                .build();


        return response.toXml();
    }

    @Operation(summary = "통화 상태 업데이트", description = "Twilio 통화 상태 변경을 처리합니다")
    @PostMapping("/status")
    public void status(
            @RequestParam String CallStatus,   // "initiated", "ringing", "answered", "completed"...
            @RequestParam String CallSid,
            @RequestParam(required = false) String callId // TwiML에서 넘긴 callId를 같이 받게 설정
    ) {
        if (callId == null) return; // 최소 처리(운영에서는 로깅)

        Call call = callRepository.findById(Long.valueOf(callId))
                .orElseThrow(() -> new EntityNotFoundException("Call not found: " + callId));

        switch (CallStatus) {
            case "initiated", "ringing" -> call.setStatus(REQUESTED);
            case "answered" -> {
                call.setStatus(ACCEPTED);
                if (call.getStartedAt() == null) call.setStartedAt(LocalDateTime.now());
            }
            case "completed", "canceled", "failed", "busy", "no-answer" -> {
                call.setStatus(ENDED);
                call.setEndedAt(LocalDateTime.now());
            }
        }

        callRepository.save(call);
    }

}
