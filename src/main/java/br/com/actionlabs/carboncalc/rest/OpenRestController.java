package br.com.actionlabs.carboncalc.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.actionlabs.carboncalc.dto.CarbonCalculationResultDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcRequestDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcResponseDTO;
import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoResponseDTO;
import br.com.actionlabs.carboncalc.service.CalculationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/open")
@Slf4j
public class OpenRestController {

  private final CalculationService calculationService;

  public OpenRestController(CalculationService calculationService) {
    this.calculationService = calculationService;
  }

  @PostMapping("/start-calc")
  public ResponseEntity<StartCalcResponseDTO> startCalculation(@Valid @RequestBody StartCalcRequestDTO dto) {
    log.info("Creating a new calculation...");
    StartCalcResponseDTO response = calculationService.createCalculation(dto);
    log.info("Calculation with id [{}] created successfully.", response.getId());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping("/info")
  public ResponseEntity<UpdateCalcInfoResponseDTO> updateInfo(@Valid @RequestBody UpdateCalcInfoRequestDTO dto) {
    log.info("Updating calculation with id [{}]...", dto.getId());
    UpdateCalcInfoResponseDTO response = calculationService.updateCalculation(dto);
    log.info("Calculation with id [{}] updated successfully.", dto.getId());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/result/{id}")
  public ResponseEntity<CarbonCalculationResultDTO> getResult(@PathVariable String id) {
    log.info("Obtaining result for calculation with id [{}]...", id);
    CarbonCalculationResultDTO response = calculationService.calculateCarbonFootprint(id);
    log.info("Result for calculation with id [{}] obtained.", id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
