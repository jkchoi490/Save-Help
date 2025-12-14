package com.save_help.Save_Help.transportationCall.repository;

import com.save_help.Save_Help.transportationCall.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
