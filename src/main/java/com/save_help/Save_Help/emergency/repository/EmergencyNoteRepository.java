package com.save_help.Save_Help.emergency.repository;

import com.save_help.Save_Help.emergency.entity.EmergencyNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyNoteRepository extends JpaRepository<EmergencyNote, Long> {
    List<EmergencyNote> findByEmergencyIdOrderByCreatedAtAsc(Long emergencyId);
}