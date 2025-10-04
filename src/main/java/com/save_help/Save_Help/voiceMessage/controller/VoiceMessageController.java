package com.save_help.Save_Help.voiceMessage.controller;

import com.save_help.Save_Help.voiceMessage.dto.VoiceMessageRequestDto;
import com.save_help.Save_Help.voiceMessage.dto.VoiceMessageResponseDto;
import com.save_help.Save_Help.voiceMessage.service.VoiceMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voice-messages")
@RequiredArgsConstructor
@Tag(name = "VoiceMessage API", description = "음성 메시지 API")
public class VoiceMessageController {

    private final VoiceMessageService voiceMessageService;

    @Operation(summary = "음성 메시지 저장", description = "음성 메시지를 저장합니다")
    @PostMapping
    public VoiceMessageResponseDto save(@RequestBody VoiceMessageRequestDto dto) {
        return voiceMessageService.saveVoiceMessage(dto);
    }

    @Operation(summary = "읽음처리 되지 않은 음성 메시지 조회", description = "읽음처리 되지 않은 음성 메시지를 조회합니다")
    @GetMapping("/unread/{receiverId}")
    public List<VoiceMessageResponseDto> getUnread(@PathVariable Long receiverId) {
        return voiceMessageService.getUnreadMessages(receiverId);
    }

    @Operation(summary = "음성 메시지 읽음 처리", description = "음성 메시지를 읽음처리 합니다")
    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        voiceMessageService.markAsRead(id);
    }
}