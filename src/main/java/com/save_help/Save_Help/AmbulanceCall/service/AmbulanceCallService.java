package com.save_help.Save_Help.AmbulanceCall.service;

import com.save_help.Save_Help.AmbulanceCall.repository.AmbulanceCallRepository;
import com.save_help.Save_Help.AmbulanceCall.dto.AmbulanceCallDto;
import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCall;
import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCallStatus;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AmbulanceCallService {

    private final AmbulanceCallRepository repository;

    /**
     * 응급차 호출 생성
     */
    public AmbulanceCall create(AmbulanceCallDto dto, User requester, Emergency emergency) {
        AmbulanceCall call = new AmbulanceCall();
        call.setRequester(requester);
        call.setEmergency(emergency);
        call.setRequestedAt(dto.getRequestedAt());
        call.setDispatchedAt(dto.getDispatchedAt());
        call.setArrivedAt(dto.getArrivedAt());
        call.setStatus(dto.getStatus());
        return repository.save(call);
    }

    /**
     * 단건 조회
     */
    public AmbulanceCall getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AmbulanceCall not found with id: " + id));
    }

    /**
     * 전체 조회
     */
    public List<AmbulanceCall> getAll() {
        return repository.findAll();
    }

    /**
     * 상태별 조회
     */
    public List<AmbulanceCall> getByStatus(AmbulanceCallStatus status) {
        return repository.findByStatus(status);
    }

    /**
     * 요청자별 조회
     */
    public List<AmbulanceCall> getByRequester(Long requesterId) {
        return repository.findByRequesterId(requesterId);
    }

    /**
     * 긴급 상황별 조회
     */
    public List<AmbulanceCall> getByEmergency(Long emergencyId) {
        return repository.findByEmergencyId(emergencyId);
    }

    /**
     * 상태 업데이트
     */
    public AmbulanceCall updateStatus(Long id, AmbulanceCallStatus status) {
        AmbulanceCall call = getById(id);
        call.setStatus(status);
        return repository.save(call);
    }

    /**
     * 삭제
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
