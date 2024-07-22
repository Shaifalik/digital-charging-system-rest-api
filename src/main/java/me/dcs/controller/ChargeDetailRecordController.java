package me.dcs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dcs.model.ChargeDetailRecordDTO;
import me.dcs.utils.ErrorResponse;
import me.dcs.service.ChargeDetailRecordService;
import me.dcs.utils.ValidationSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cdr")
@Tag(name = "Charge Detail Records Application", description = "RESTful API for managing CDRs.")
public class ChargeDetailRecordController {

    @Autowired
    ChargeDetailRecordService chargeDetailRecordsService;

    @PostMapping("/create")
    @Operation(summary = "Create a CDR", description = "API to create a new CDR and returns the created CDR on success")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CDR created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid CDR data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ChargeDetailRecordDTO> createCDR(@Validated(ValidationSequence.class)
                                                           @RequestBody ChargeDetailRecordDTO cdrDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chargeDetailRecordsService.save(cdrDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a CDR by ID", description = "API to fetch a specific CDR based on its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "No CDR found by Id", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ChargeDetailRecordDTO> getCDRById(@PathVariable(name = "id") Long sessionId) {
        return ResponseEntity.status(HttpStatus.OK).body(chargeDetailRecordsService.findBySessionId(sessionId));
    }

    @GetMapping("/search/{vehicleId}")
    @Operation(summary = "Search CDRs by Vehicle ID", description = "API to get all CDRs for a Vehicle ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All CDRs fetched successfully"),
            @ApiResponse(responseCode = "404", description = "No CDRs found by Vehicle Id", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ChargeDetailRecordDTO>> getCDRsByVehicleId(@PathVariable(name = "vehicleId") String vehicleId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(chargeDetailRecordsService.findAllByVehicleId(vehicleId));
    }


}
