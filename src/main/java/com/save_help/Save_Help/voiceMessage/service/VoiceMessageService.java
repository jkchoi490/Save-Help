package com.save_help.Save_Help.voiceMessage.service;

import com.save_help.Save_Help.voiceMessage.dto.VoiceMessageRequestDto;
import com.save_help.Save_Help.voiceMessage.dto.VoiceMessageResponseDto;
import com.save_help.Save_Help.voiceMessage.entity.VoiceMessage;
import com.save_help.Save_Help.voiceMessage.repository.VoiceMessageRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VoiceMessageService {

    private final VoiceMessageRepository voiceMessageRepository;
    private final UserRepository userRepository;

    // 음성 메시지 저장
    public VoiceMessageResponseDto saveVoiceMessage(VoiceMessageRequestDto dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("발신자 없음"));
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("수신자 없음"));

        VoiceMessage voiceMessage = new VoiceMessage();
        voiceMessage.setSender(sender);
        voiceMessage.setReceiver(receiver);
        voiceMessage.setFileUrl(dto.getFileUrl());
        voiceMessage.setDuration(dto.getDuration());
        voiceMessage.setSentAt(LocalDateTime.now());
        voiceMessage.setRead(false);

        VoiceMessage saved = voiceMessageRepository.save(voiceMessage);

        VoiceMessageResponseDto response = new VoiceMessageResponseDto();
        response.setId(saved.getId());
        response.setSenderId(sender.getId());
        response.setReceiverId(receiver.getId());
        response.setFileUrl(saved.getFileUrl());
        response.setDuration(saved.getDuration());
        response.setSentAt(saved.getSentAt());
        response.setRead(saved.isRead());
        response.setTranscript(transcribe(saved.getFileUrl())); // 음성 인식 (가짜 구현)

        return response;
    }

    // 안 읽은 메시지 조회
    public List<VoiceMessageResponseDto> getUnreadMessages(Long receiverId) {
        return voiceMessageRepository.findByReceiverIdAndReadFalse(receiverId)
                .stream().map(vm -> {
                    VoiceMessageResponseDto dto = new VoiceMessageResponseDto();
                    dto.setId(vm.getId());
                    dto.setSenderId(vm.getSender().getId());
                    dto.setReceiverId(vm.getReceiver().getId());
                    dto.setFileUrl(vm.getFileUrl());
                    dto.setDuration(vm.getDuration());
                    dto.setSentAt(vm.getSentAt());
                    dto.setRead(vm.isRead());
                    dto.setTranscript(transcribe(vm.getFileUrl()));
                    return dto;
                }).collect(Collectors.toList());
    }


    // 읽음 처리
    public void markAsRead(Long id) {
        VoiceMessage vm = voiceMessageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("메시지 없음"));
        vm.setRead(true);
        voiceMessageRepository.save(vm);
    }

    // STT (Speech-To-Text) – Google STT 연동
    private String transcribe(String fileUrl) {
        // 계속 개발 예정
        return "[음성 인식 결과: " + fileUrl + "]";
    }

    //음성 인식 결과 내용 저장 & 조회 기능 개발
}
