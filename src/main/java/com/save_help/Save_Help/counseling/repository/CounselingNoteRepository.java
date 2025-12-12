package com.save_help.Save_Help.counseling.repository;

import com.save_help.Save_Help.counseling.entity.CounselingNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounselingNoteRepository extends JpaRepository<CounselingNote, Long> {
    Optional<CounselingNote> findByCounselingId(Long counselingId);
    boolean existsByCounselingId(Long counselingId);
}