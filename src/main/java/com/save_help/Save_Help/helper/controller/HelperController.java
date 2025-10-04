package com.save_help.Save_Help.helper.controller;

import com.save_help.Save_Help.helper.dto.HelperRequestDto;
import com.save_help.Save_Help.helper.dto.HelperResponseDto;
import com.save_help.Save_Help.helper.entity.HelperRole;
import com.save_help.Save_Help.helper.service.HelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpers")
@RequiredArgsConstructor
public class HelperController {

    private final HelperService helperService;

    // 생성
    @PostMapping
    public ResponseEntity<HelperResponseDto> create(@RequestBody HelperRequestDto dto) {
        return ResponseEntity.ok(helperService.createHelper(dto));
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<HelperResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(helperService.getHelper(id));
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<HelperResponseDto>> getAll() {
        return ResponseEntity.ok(helperService.getAllHelpers());
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<HelperResponseDto> update(@PathVariable Long id,
                                                    @RequestBody HelperRequestDto dto) {
        return ResponseEntity.ok(helperService.updateHelper(id, dto));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        helperService.deleteHelper(id);
        return ResponseEntity.noContent().build();
    }

    // 역할별 조회
    @GetMapping("/role/{role}")
    public ResponseEntity<List<HelperResponseDto>> getByRole(@PathVariable HelperRole role) {
        return ResponseEntity.ok(helperService.getHelpersByRole(role));
    }

    // 근무 가능자만 조회
    @GetMapping("/available")
    public ResponseEntity<List<HelperResponseDto>> getAvailableHelpers() {
        return ResponseEntity.ok(helperService.getAvailableHelpers());
    }

    // 역할별 + 근무 가능자 조회
    @GetMapping("/role/{role}/available")
    public ResponseEntity<List<HelperResponseDto>> getAvailableByRole(@PathVariable HelperRole role) {
        return ResponseEntity.ok(helperService.getAvailableHelpersByRole(role));
    }
}
