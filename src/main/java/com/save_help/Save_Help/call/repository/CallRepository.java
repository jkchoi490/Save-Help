package com.save_help.Save_Help.call.repository;

import com.save_help.Save_Help.call.entity.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call, Long> {
}
