package com.save_help.Save_Help.dailyNecessities.controller;

import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import com.save_help.Save_Help.dailyNecessities.service.DailyNecessitiesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/necessities")
public class DailyNecessitiesController {

    private final DailyNecessitiesService necessitiesService;

    public DailyNecessitiesController(DailyNecessitiesService necessitiesService) {
        this.necessitiesService = necessitiesService;
    }

    //전체 목록 조회
    @Operation(summary = "생필품 전체 목록 조회", description = "생필품 전체 목록을 조회합니다")
    @GetMapping
    public List<DailyNecessities> getAll() {
        return necessitiesService.getAll();
    }

    //카테고리별 조회
    @Operation(summary = "생필품 카테고리별 조회", description = "생필품을 카테고리별로 조회합니다")
    @GetMapping("/category/{category}")
    public List<DailyNecessities> getByCategory(@PathVariable NecessityCategory category) {
        return necessitiesService.getByCategory(category);
    }

    //이름 검색
    @Operation(summary = "생필품 이름 검색", description = "생필품을 이름으로 검색합니다")
    @GetMapping("/search")
    public List<DailyNecessities> searchByName(@RequestParam String name) {
        return necessitiesService.searchByName(name);
    }

    //생성
    @Operation(summary = "생필품 생성", description = "생필품을 생성합니다")
    @PostMapping
    public DailyNecessities create(@RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.create(dto);
    }

    //수정
    @Operation(summary = "생필품 수정", description = "생필품을 수정합니다")
    @PutMapping("/{id}")
    public DailyNecessities update(@PathVariable Long id, @RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.update(id, dto);
    }

    //삭제(비활성화)
    @Operation(summary = "생필품 비활성화", description = "생필품을 비활성화합니다")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        necessitiesService.delete(id);
    }
}
