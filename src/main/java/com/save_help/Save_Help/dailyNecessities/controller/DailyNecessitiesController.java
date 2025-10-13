package com.save_help.Save_Help.dailyNecessities.controller;

import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import com.save_help.Save_Help.dailyNecessities.service.DailyNecessitiesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/necessities")
public class DailyNecessitiesController {

    private final DailyNecessitiesService necessitiesService;

    public DailyNecessitiesController(DailyNecessitiesService necessitiesService) {
        this.necessitiesService = necessitiesService;
    }

    @Operation(summary = "생필품 전체 목록 조회", description = "생필품 전체 목록을 조회합니다")
    @GetMapping("/getAll")
    public List<DailyNecessitiesDto> getAll() {
        return necessitiesService.getAll();
    }

    @Operation(summary = "생필품 단건 조회", description = "ID로 특정 생필품을 조회합니다")
    @GetMapping("/{id}")
    public DailyNecessitiesDto getById(@PathVariable Long id) {
        return necessitiesService.getById(id);
    }

    @Operation(summary = "생필품 카테고리별 조회", description = "생필품을 카테고리별로 조회합니다")
    @GetMapping("/category/{category}")
    public List<DailyNecessitiesDto> getByCategory(@PathVariable NecessityCategory category) {
        return necessitiesService.getByCategory(category);
    }

    @Operation(summary = "생필품 이름 검색", description = "생필품을 이름으로 검색합니다")
    @GetMapping("/search")
    public List<DailyNecessitiesDto> searchByName(@RequestParam String name) {
        return necessitiesService.searchByName(name);
    }

    @Operation(summary = "생필품 생성", description = "생필품을 생성합니다")
    @PostMapping
    public DailyNecessitiesDto create(@RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.create(dto);
    }

    @Operation(summary = "생필품 수정", description = "생필품을 수정합니다")
    @PutMapping("/{id}")
    public DailyNecessitiesDto update(@PathVariable Long id, @RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.update(id, dto);
    }

    @Operation(summary = "생필품 비활성화", description = "생필품을 비활성화합니다")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        necessitiesService.delete(id);
    }

    @Operation(summary = "센터의 생필품 목록 조회", description = "특정 센터에 등록된 모든 생필품을 조회합니다.")
    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<DailyNecessitiesDto>> getByCenter(@PathVariable Long centerId) {
        List<DailyNecessitiesDto> list = necessitiesService.getByCenter(centerId);
        return ResponseEntity.ok(list);
    }
}
