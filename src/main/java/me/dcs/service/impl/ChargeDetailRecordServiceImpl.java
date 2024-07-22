package me.dcs.service.impl;

import me.dcs.exception.InvalidCDRException;
import me.dcs.exception.NotFoundException;
import me.dcs.model.ChargeDetailRecord;
import me.dcs.model.ChargeDetailRecordDTO;
import me.dcs.repository.ChargeDetailRecordRepository;
import me.dcs.service.ChargeDetailRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.dcs.utils.ApplicationConstants.*;

@Service
@Transactional
public class ChargeDetailRecordServiceImpl implements ChargeDetailRecordService {

    @Autowired
    private ChargeDetailRecordRepository chargeDetailRecordsRepository;

    @Transactional(readOnly = true)
    public ChargeDetailRecordDTO findBySessionId(Long sessionId) {
        return this.chargeDetailRecordsRepository.findById(sessionId)
                .map(ChargeDetailRecord::convertToDTO).orElseThrow(() -> new NotFoundException(INVALID_SESSION_ID));
    }

    public List<ChargeDetailRecordDTO> findAllByVehicleId(String vehicleIdentification) {
        return this.chargeDetailRecordsRepository
                .findAllByVehicleIdentification(vehicleIdentification, Sort.by(Sort.Direction.ASC, "endTime")).map(
                        cdrList -> cdrList.stream().map(ChargeDetailRecord::convertToDTO).collect(Collectors.toList()))
                .orElseThrow(() ->
                        new NotFoundException(INVALID_VEHICLE_ID));
    }

    public ChargeDetailRecordDTO save(ChargeDetailRecordDTO chargeDetailRecordDTO) {
        Optional<ChargeDetailRecord> chargeDetailRecord = this.chargeDetailRecordsRepository.findLatestRecordOfVehicleId(chargeDetailRecordDTO.getVehicleIdentification());
        if (chargeDetailRecord.isPresent() && chargeDetailRecordDTO.getStartTime().isBefore(chargeDetailRecord.get().getEndTime())) {
            throw new InvalidCDRException(OUTDATED_RECORD);
        }
        return chargeDetailRecordsRepository.save(chargeDetailRecordDTO.convertToDO()).convertToDTO();
    }
}