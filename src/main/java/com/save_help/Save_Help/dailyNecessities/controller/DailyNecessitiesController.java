package com.save_help.Save_Help.dailyNecessities.controller;

import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import com.save_help.Save_Help.dailyNecessities.service.DailyNecessitiesService;
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
    @GetMapping
    public List<DailyNecessities> getAll() {
        return necessitiesService.getAll();
    }

    //카테고리별 조회
    @GetMapping("/category/{category}")
    public List<DailyNecessities> getByCategory(@PathVariable NecessityCategory category) {
        return necessitiesService.getByCategory(category);
    }

    //이름 검색
    @GetMapping("/search")
    public List<DailyNecessities> searchByName(@RequestParam String name) {
        return necessitiesService.searchByName(name);
    }

    //생성
    @PostMapping
    public DailyNecessities create(@RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.create(dto);
    }

    //수정
    @PutMapping("/{id}")
    public DailyNecessities update(@PathVariable Long id, @RequestBody DailyNecessitiesDto dto) {
        return necessitiesService.update(id, dto);
    }

    //삭제(비활성화)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        necessitiesService.delete(id);
    }
}
