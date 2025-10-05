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
@Tag(name = "CommunityCenter", description = "지역 커뮤니티 센터 관리 API")
public class CommunityCenterController {

    private final CommunityCenterService service;

    @Operation(summary = "모든 센터 조회", description = "모든 센터를 조회합니다")
    @GetMapping
    public ResponseEntity<List<CommunityCenter>> getAllCenters() {
        return ResponseEntity.ok(service.getAllCenters());
    }

    @Operation(summary = "ID로 센터 조회", description = "ID로 센터를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<CommunityCenter> getCenterById(@PathVariable Long id) {
        return service.getCenterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "센터 생성", description = "센터를 생성합니다")
    @PostMapping
    public ResponseEntity<CommunityCenter> createCenter(@RequestBody CommunityCenterDto dto) {
        return ResponseEntity.ok(service.createCenter(dto));
    }

    @Operation(summary = "센터 수정", description = "센터를 수정합니다")
    @PutMapping("/{id}")
    public ResponseEntity<CommunityCenter> updateCenter(@PathVariable Long id, @RequestBody CommunityCenterDto dto) {
        return ResponseEntity.ok(service.updateCenter(id, dto));
    }

    @Operation(summary = "센터 삭제", description = "센터를 삭제합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long id) {
        service.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "타입별 센터 조회", description = "타입별 센터를 조회합니다")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CommunityCenter>> getCentersByType(@PathVariable CenterType type) {
        return ResponseEntity.ok(service.getCentersByType(type));
    }

    @Operation(summary = "운영 중(active=true) 센터만 조회", description = "운영중인 센터만 조회합니다")
    @GetMapping("/active")
    public ResponseEntity<List<CommunityCenter>> getActiveCenters() {
        return ResponseEntity.ok(service.getActiveCenters());
    }

    @Operation(summary = "운영 중(active=true) + 타입별 센터 조회", description = "운영중인 타입별 센터를 조회합니다")
    @GetMapping("/active/type/{type}")
    public ResponseEntity<List<CommunityCenter>> getActiveCentersByType(@PathVariable CenterType type) {
        return ResponseEntity.ok(service.getActiveCentersByType(type));
    }
}
