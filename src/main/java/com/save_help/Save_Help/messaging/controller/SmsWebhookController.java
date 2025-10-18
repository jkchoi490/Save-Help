package com.save_help.Save_Help.messaging.controller;

import com.save_help.Save_Help.messaging.service.DailyNecessitiesMessageService;
import com.save_help.Save_Help.messaging.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsWebhookController {

    private final SmsService smsService;
    private final DailyNecessitiesMessageService dailyNecessitiesMessageService;

    //Twilio → 서버 (사용자 메시지 수신)
    @PostMapping("/webhook")
    public void receiveSms(@RequestParam("From") String from,
                           @RequestParam("Body") String body) {
        smsService.handleIncomingSms(from, body);
    }


    //  Helper → 사용자 회신
    @PostMapping("/reply")
    public void reply(@RequestParam("helperPhone") String helperPhone,
                      @RequestParam("to") String toUser,
                      @RequestParam("message") String message) {
        smsService.handleHelperReply(helperPhone, toUser, message);
    }

    @PostMapping("/webhook/receiveSmsDailyNecessities")
    public ResponseEntity<String> receiveSmsDailyNecessities(
            @RequestParam("From") String from,
            @RequestParam("To") String to,
            @RequestParam("Body") String body) {

        dailyNecessitiesMessageService.handleIncomingSms(from, to, body);
        return ResponseEntity.ok("Message received");
    }
}