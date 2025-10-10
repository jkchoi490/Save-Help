package com.save_help.Save_Help.nationalSubsidy.controller;

import com.save_help.Save_Help.nationalSubsidy.dto.NationalSubsidyResponseDto;
import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyService;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import com.save_help.Save_Help.nationalSubsidy.dto.NationalSubsidyRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subsidies")
@RequiredArgsConstructor
public class NationalSubsidyController {

    private final NationalSubsidyService service;

    // 보조금 등록
    @Operation(summary = "보조금 등록", description = "보조금을 등록합니다")
    @PostMapping
    public NationalSubsidyResponseDto create(@RequestBody NationalSubsidyRequestDto dto) {
        return service.create(dto);
    }

    // 전체 조회
    @Operation(summary = "보조금 전체 조회", description = "보조금 전체를 조회합니다")
    @GetMapping
    public List<NationalSubsidyResponseDto> getAll() {
        return service.findAll();
    }

    // 단일 조회
    @Operation(summary = "보조금 단일 조회", description = "보조금을 단일 조회합니다")
    @GetMapping("/{id}")
    public NationalSubsidyResponseDto getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // 유형별 조회
    @Operation(summary = "보조금 유형별 조회", description = "보조금을 유형별로 조회합니다")
    @GetMapping("/type/{type}")
    public List<NationalSubsidyResponseDto> getByType(@PathVariable SubsidyType type) {
        return service.findByType(type);
    }

    // 이름 검색
    @Operation(summary = "보조금 이름으로 검색", description = "보조금을 이름으로 검색합니다")
    @GetMapping("/search")
    public List<NationalSubsidyResponseDto> searchByName(@RequestParam String keyword) {
        return service.searchByName(keyword);
    }

    // 수정
    @Operation(summary = "보조금 수정", description = "보조금을 수정합니다")
    @PutMapping("/{id}")
    public NationalSubsidyResponseDto update(@PathVariable Long id, @RequestBody NationalSubsidyRequestDto dto) {
        return service.update(id, dto);
    }

    // 삭제
    @Operation(summary = "보조금 삭제", description = "보조금을 삭제합니다")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}