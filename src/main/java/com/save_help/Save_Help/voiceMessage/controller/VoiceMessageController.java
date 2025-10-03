package com.save_help.Save_Help.voiceMessage.controller;

import com.save_help.Save_Help.voiceMessage.dto.VoiceMessageRequestDto;
import com.save_help.Save_Help.voiceMessage.dto.VoiceMessageResponseDto;
import com.save_help.Save_Help.voiceMessage.service.VoiceMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voice-messages")
@RequiredArgsConstructor
public class VoiceMessageController {

    private final VoiceMessageService voiceMessageService;

    @PostMapping
    public VoiceMessageResponseDto save(@RequestBody VoiceMessageRequestDto dto) {
        return voiceMessageService.saveVoiceMessage(dto);
    }

    @GetMapping("/unread/{receiverId}")
    public List<VoiceMessageResponseDto> getUnread(@PathVariable Long receiverId) {
        return voiceMessageService.getUnreadMessages(receiverId);
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        voiceMessageService.markAsRead(id);
    }
}