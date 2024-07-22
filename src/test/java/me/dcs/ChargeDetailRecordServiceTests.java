package me.dcs;

import me.dcs.exception.InvalidCDRException;
import me.dcs.exception.NotFoundException;
import me.dcs.model.ChargeDetailRecord;
import me.dcs.model.ChargeDetailRecordDTO;
import me.dcs.repository.ChargeDetailRecordRepository;
import me.dcs.service.impl.ChargeDetailRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static me.dcs.utils.ApplicationConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChargeDetailRecordServiceTests {

    private final static String START_TIME = "2024-07-17T10:15:30";
    private final static String END_TIME = "2024-07-17T14:15:30";
    private final static String VEHICLE_IDENTIFICATION = "12341234ABC";
    private final static Long SESSION_IDENTIFICATION = 1L;
    private final static double cost = 15.50;

    @Mock
    private ChargeDetailRecordRepository chargeDetailRecordRepository;

    @InjectMocks
    private ChargeDetailRecordServiceImpl chargeDetailRecordSvc;

    private ChargeDetailRecord cdrDO;

    @BeforeEach
    void setUp() {
        cdrDO = ChargeDetailRecord.builder().sessionIdentification(SESSION_IDENTIFICATION)
                .vehicleIdentification(VEHICLE_IDENTIFICATION).
                        startTime(LocalDateTime.parse(START_TIME)).
                        endTime(LocalDateTime.parse(END_TIME)).cost(cost).build();
    }

    @Test
    public void saveCDR_ShouldReturnSuccess() {
        when(chargeDetailRecordRepository.findLatestRecordOfVehicleId(cdrDO.getVehicleIdentification()))
                .thenReturn(Optional.empty());
        when(chargeDetailRecordRepository.save(any(ChargeDetailRecord.class))).thenReturn(cdrDO);
        ChargeDetailRecordDTO savedCDR = chargeDetailRecordSvc.save(cdrDO.convertToDTO());
        assertThat(savedCDR).isNotNull();
        assertEquals(savedCDR.getVehicleIdentification(), cdrDO.getVehicleIdentification());
    }

    @Test
    public void saveCDR_ShouldReturnFailure_WhenEndTimeIsBeforeStartTime() {
        when(chargeDetailRecordRepository.findLatestRecordOfVehicleId(cdrDO.getVehicleIdentification()))
                .thenReturn(Optional.of(cdrDO));
        InvalidCDRException exception =
                assertThrows(InvalidCDRException.class,
                        () -> chargeDetailRecordSvc.save(cdrDO.convertToDTO()), "Expected to throw ex, but it didn't");
        assertEquals(exception.getMessage(), OUTDATED_RECORD);
    }

    @Test
    public void findCDR_ShouldReturnSuccess() {

        when(chargeDetailRecordRepository.findById(cdrDO.getSessionIdentification()))
                .thenReturn(Optional.of(cdrDO));
        ChargeDetailRecordDTO savedCDR = chargeDetailRecordSvc.findBySessionId(cdrDO.getSessionIdentification());
        assertThat(savedCDR).isNotNull();
        assertEquals(savedCDR.getVehicleIdentification(), cdrDO.getVehicleIdentification());
        assertEquals(savedCDR.getStartTime(), cdrDO.getStartTime());
        assertEquals(savedCDR.getEndTime(), cdrDO.getEndTime());
        assertEquals(savedCDR.getCost(), cdrDO.getCost());
    }

    @Test
    public void findCDR_ShouldReturnFailure_NotFound() {

        when(chargeDetailRecordRepository.findById(cdrDO.getSessionIdentification()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> chargeDetailRecordSvc.findBySessionId(cdrDO.getSessionIdentification()));
        assertEquals(exception.getMessage(), INVALID_SESSION_ID);
    }

    @Test
    public void SearchAllCDRs_ShouldReturnFailure_NotFound() {
        when(chargeDetailRecordRepository.findAllByVehicleIdentification(
                cdrDO.getVehicleIdentification(), Sort.by(Sort.Direction.ASC, "endTime")))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> chargeDetailRecordSvc.findAllByVehicleId(cdrDO.getVehicleIdentification()));
        assertEquals(exception.getMessage(), INVALID_VEHICLE_ID);
    }

    @Test
    public void SearchAllCDRs_ShouldReturnSuccess() {
        when(chargeDetailRecordRepository.findAllByVehicleIdentification(
                cdrDO.getVehicleIdentification(), Sort.by(Sort.Direction.ASC, "endTime")))
                .thenReturn(Optional.of(List.of(cdrDO)));

        List<ChargeDetailRecordDTO> savedCDR = chargeDetailRecordSvc.findAllByVehicleId(cdrDO.getVehicleIdentification());

        assertEquals(savedCDR.size(), 1);
        assertEquals(savedCDR.get(0).getVehicleIdentification(), cdrDO.getVehicleIdentification());
    }


}
