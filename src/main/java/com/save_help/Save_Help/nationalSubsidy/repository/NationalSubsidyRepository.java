package com.save_help.Save_Help.nationalSubsidy.repository;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NationalSubsidyRepository extends JpaRepository<NationalSubsidy, Long> {

    // 타입별 검색
    List<NationalSubsidy> findByType(SubsidyType type);

    // 활성화된 보조금만 조회
    List<NationalSubsidy> findByActiveTrue();

    // 이름 키워드 검색
    List<NationalSubsidy> findByNameContainingIgnoreCase(String keyword);
}

