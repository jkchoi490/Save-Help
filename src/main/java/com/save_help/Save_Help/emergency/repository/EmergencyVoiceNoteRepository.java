package com.save_help.Save_Help.emergency.repository;

import com.save_help.Save_Help.emergency.entity.EmergencyVoiceNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyVoiceNoteRepository extends JpaRepository<EmergencyVoiceNote, Long> {
    List<EmergencyVoiceNote> findByEmergencyIdOrderByCreatedAtAsc(Long emergencyId);
}