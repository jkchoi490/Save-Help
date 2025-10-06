package com.save_help.Save_Help.transportationCall.repository;

import com.save_help.Save_Help.transportationCall.entity.TransportationCall;
import com.save_help.Save_Help.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportationCallRepository extends JpaRepository<TransportationCall, Long> {
    List<TransportationCall> findByRequester(User requester);
}
