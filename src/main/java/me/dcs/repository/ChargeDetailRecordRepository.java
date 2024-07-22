package me.dcs.repository;

import me.dcs.model.ChargeDetailRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChargeDetailRecordRepository extends JpaRepository<ChargeDetailRecord, Long> {

    @Query(value = "SELECT * FROM charge_detail_record e where e.vehicle_identification=?1 ORDER BY e.end_time DESC LIMIT 1", nativeQuery = true)
    Optional<ChargeDetailRecord> findLatestRecordOfVehicleId(String vehicleId);

    Optional<List<ChargeDetailRecord>> findAllByVehicleIdentification(String vehicleIdentification, Sort sort);
}
