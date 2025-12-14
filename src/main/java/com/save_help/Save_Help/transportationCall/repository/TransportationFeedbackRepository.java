package com.save_help.Save_Help.transportationCall.repository;

import com.save_help.Save_Help.transportationCall.entity.TransportationFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransportationFeedbackRepository extends JpaRepository<TransportationFeedback, Long> {
    Optional<TransportationFeedback> findByCallId(Long callId);
}