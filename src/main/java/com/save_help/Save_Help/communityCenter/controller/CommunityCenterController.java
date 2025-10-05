package com.save_help.Save_Help.communityCenter.controller;

import com.save_help.Save_Help.communityCenter.dto.CommunityCenterDto;
import com.save_help.Save_Help.communityCenter.entity.CenterType;
import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.service.CommunityCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community-centers")
@RequiredArgsConstructor
public class CommunityCenterController {

    private final CommunityCenterService service;

    @GetMapping
    public ResponseEntity<List<CommunityCenter>> getAllCenters() {
        return ResponseEntity.ok(service.getAllCenters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityCenter> getCenterById(@PathVariable Long id) {
        return service.getCenterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CommunityCenter> createCenter(@RequestBody CommunityCenterDto dto) {
        return ResponseEntity.ok(service.createCenter(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityCenter> updateCenter(@PathVariable Long id, @RequestBody CommunityCenterDto dto) {
        return ResponseEntity.ok(service.updateCenter(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long id) {
        service.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CommunityCenter>> getCentersByType(@PathVariable CenterType type) {
        return ResponseEntity.ok(service.getCentersByType(type));
    }

    @GetMapping("/active")
    public ResponseEntity<List<CommunityCenter>> getActiveCenters() {
        return ResponseEntity.ok(service.getActiveCenters());
    }

    @GetMapping("/active/type/{type}")
    public ResponseEntity<List<CommunityCenter>> getActiveCentersByType(@PathVariable CenterType type) {
        return ResponseEntity.ok(service.getActiveCentersByType(type));
    }
}
