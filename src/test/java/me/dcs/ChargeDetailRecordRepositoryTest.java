package me.dcs;

import me.dcs.model.ChargeDetailRecord;
import me.dcs.repository.ChargeDetailRecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static me.dcs.utils.ApplicationTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChargeDetailRecordRepositoryTest {

    @Autowired
    private ChargeDetailRecordRepository chargeDetailRecordRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveCDRTest() {
        ChargeDetailRecord cdrDO = ChargeDetailRecord.builder()
                .vehicleIdentification(VEHICLE_IDENTIFICATION).
                        startTime(LocalDateTime.parse(START_TIME)).
                        endTime(LocalDateTime.parse(END_TIME)).cost(cost).build();
        chargeDetailRecordRepository.save(cdrDO);

        ChargeDetailRecord cdrDO2 = ChargeDetailRecord.builder()
                .vehicleIdentification(VEHICLE_IDENTIFICATION).
                        startTime(LocalDateTime.parse(START_TIME_2)).
                        endTime(LocalDateTime.parse(END_TIME_2)).cost(cost_2).build();
        chargeDetailRecordRepository.save(cdrDO2);

        Assertions.assertThat(cdrDO.getSessionIdentification()).isGreaterThan(0);
        Assertions.assertThat(cdrDO2.getSessionIdentification()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getCDRBySessionId() {
        ChargeDetailRecord cdr = chargeDetailRecordRepository.findById(1L).get();
        assertThat(cdr).isNotNull();
        assertThat(cdr.getVehicleIdentification()).isEqualTo(VEHICLE_IDENTIFICATION);
        assertThat(cdr.getCost()).isEqualTo(cost);
    }

    @Test
    @Order(3)
    public void getLatestRecordByVehicleId() {
        ChargeDetailRecord cdr = chargeDetailRecordRepository.findLatestRecordOfVehicleId(VEHICLE_IDENTIFICATION).get();

        assertThat(cdr).isNotNull();
        assertThat(cdr.getStartTime()).isEqualTo(START_TIME_2);
        assertThat(cdr.getEndTime()).isEqualTo(END_TIME_2);
        assertThat(cdr.getCost()).isEqualTo(cost_2);
    }

    @Test
    @Order(4)
    public void getAllLatestRecordByVehicleId() {
        List<ChargeDetailRecord> cdr = chargeDetailRecordRepository
                .findAllByVehicleIdentification(VEHICLE_IDENTIFICATION, Sort.by(Sort.Direction.ASC, "endTime")).get();
        assertThat(cdr).isNotNull();
        assertThat(cdr.size()).isEqualTo(2);
        assertThat(cdr.get(0).getCost()).isEqualTo(cost);
        assertThat(cdr.get(1).getCost()).isEqualTo(cost_2);
    }

}
