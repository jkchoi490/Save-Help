package com.save_help.Save_Help.AmbulanceCall.controller;

import com.save_help.Save_Help.AmbulanceCall.dto.AmbulanceCallDto;
import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCall;
import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCallStatus;
import com.save_help.Save_Help.AmbulanceCall.service.AmbulanceCallService;
import com.save_help.Save_Help.emergency.Emergency;
import com.save_help.Save_Help.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance-calls")
@RequiredArgsConstructor
public class AmbulanceCallController {

    private final AmbulanceCallService service;
    private final EntityManager em; // 단순 연관 조회용 (실제 구현 시 Service로 대체 가능)


    @PostMapping
    public ResponseEntity<AmbulanceCall> create(@RequestBody AmbulanceCallDto dto) {
        User requester = em.find(User.class, dto.getRequesterId());
        Emergency emergency = em.find(Emergency.class, dto.getEmergencyId());
        return ResponseEntity.ok(service.create(dto, requester, emergency));
    }

    @GetMapping
    public ResponseEntity<List<AmbulanceCall>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<AmbulanceCall> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<AmbulanceCall>> getByStatus(@PathVariable AmbulanceCallStatus status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AmbulanceCall>> getByRequester(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByRequester(userId));
    }


    @GetMapping("/emergency/{emergencyId}")
    public ResponseEntity<List<AmbulanceCall>> getByEmergency(@PathVariable Long emergencyId) {
        return ResponseEntity.ok(service.getByEmergency(emergencyId));
    }


    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<AmbulanceCall> updateStatus(@PathVariable Long id, @PathVariable AmbulanceCallStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
