package com.save_help.Save_Help.messaging.controller;

import com.save_help.Save_Help.messaging.entity.DailyNecessitiesMessage;
import com.save_help.Save_Help.messaging.service.DailyNecessitiesMessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class DailyNecessitiesMessageController {

    private final DailyNecessitiesMessageService messageService;

    @PostMapping("/reply/{sessionId}")
    public ResponseEntity<String> sendReply(@PathVariable Long sessionId,
                                            @RequestParam String content) {
        messageService.sendReply(sessionId, content);
        return ResponseEntity.ok("Reply sent");
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<DailyNecessitiesMessage>> getMessages(@PathVariable Long sessionId) {
        return ResponseEntity.ok(messageService.getMessages(sessionId));
    }
}
