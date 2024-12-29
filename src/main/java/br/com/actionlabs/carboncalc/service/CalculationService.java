package br.com.actionlabs.carboncalc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.actionlabs.carboncalc.dto.CarbonCalculationResultDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcRequestDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcResponseDTO;
import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoResponseDTO;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.exception.CalculationNotFoundException;
import br.com.actionlabs.carboncalc.model.Calculation;
import br.com.actionlabs.carboncalc.model.EnergyEmissionFactor;
import br.com.actionlabs.carboncalc.model.SolidWasteEmissionFactor;
import br.com.actionlabs.carboncalc.repository.CalculationRepository;
import br.com.actionlabs.carboncalc.repository.EnergyEmissionFactorRepository;
import br.com.actionlabs.carboncalc.repository.SolidWasteEmissionFactorRepository;
import br.com.actionlabs.carboncalc.repository.TransportationEmissionFactorRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CalculationService {

    private final CalculationRepository calculationRepository;
    private final EnergyEmissionFactorRepository energyEmissionFactorRepository;
    private final TransportationEmissionFactorRepository transportationEmissionFactorRepository;
    private final SolidWasteEmissionFactorRepository solidWasteEmissionFactorRepository;

    public CalculationService(CalculationRepository calculationRepository,
            EnergyEmissionFactorRepository energyEmissionFactorRepository,
            TransportationEmissionFactorRepository transportationEmissionFactorRepository,
            SolidWasteEmissionFactorRepository solidWasteEmissionFactorRepository) {
        this.calculationRepository = calculationRepository;
        this.energyEmissionFactorRepository = energyEmissionFactorRepository;
        this.transportationEmissionFactorRepository = transportationEmissionFactorRepository;
        this.solidWasteEmissionFactorRepository = solidWasteEmissionFactorRepository;
    }

    public StartCalcResponseDTO createCalculation(StartCalcRequestDTO dto) {

        Calculation calculation = new Calculation();
        calculation.setId(UUID.randomUUID().toString());
        calculation.setName(dto.getName());
        calculation.setEmail(dto.getEmail());
        calculation.setUf(dto.getUf().toUpperCase());
        calculation.setPhoneNumber(dto.getPhoneNumber());

        boolean success = saveCalculation(calculation);

        return new StartCalcResponseDTO(success ? calculation.getId() : null);
    }

    public UpdateCalcInfoResponseDTO updateCalculation(UpdateCalcInfoRequestDTO dto) {

        Calculation calculation = findCalculationById(dto.getId());

        calculation.setEnergyConsumption(dto.getEnergyConsumption());
        calculation.setTransportation(dto.getTransportation());
        calculation.setSolidWasteTotal(dto.getSolidWasteTotal());
        calculation.setRecyclePercentage(dto.getRecyclePercentage());

        boolean success = saveCalculation(calculation);

        return new UpdateCalcInfoResponseDTO(success);
    }

    public CarbonCalculationResultDTO calculateCarbonFootprint(String id) {

        Calculation calculation = findCalculationById(id);

        BigDecimal energyEmissions = calculateEnergyEmissions(calculation);
        BigDecimal transportationEmissions = calculateTransportationEmissions(calculation);
        BigDecimal solidWasteEmissions = calculateSolidWasteEmissions(calculation);
        BigDecimal total = energyEmissions.add(transportationEmissions).add(solidWasteEmissions)
                .setScale(2, RoundingMode.HALF_UP);

        return new CarbonCalculationResultDTO(
                energyEmissions.doubleValue(),
                transportationEmissions.doubleValue(),
                solidWasteEmissions.doubleValue(),
                total.doubleValue());
    }

    private BigDecimal calculateTransportationEmissions(Calculation calculation) {

        Map<TransportationType, BigDecimal> transportationFactors = new HashMap<>();

        calculation.getTransportation().stream()
                .map(transportation -> transportationEmissionFactorRepository
                .findById(transportation.getType()).get())
                .forEach(transportationFactor -> transportationFactors.put(
                transportationFactor.getType(),
                BigDecimal.valueOf(transportationFactor.getFactor())));

        BigDecimal transportationEmissions = calculation.getTransportation().stream()
                .map(transportation -> {
                    BigDecimal factor = transportationFactors.get(transportation.getType());
                    return BigDecimal.valueOf(transportation.getMonthlyDistance()).multiply(factor);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return transportationEmissions;
    }

    private BigDecimal calculateSolidWasteEmissions(Calculation calculation) {

        SolidWasteEmissionFactor solidWasteFactor = solidWasteEmissionFactorRepository
                .findById(calculation.getUf()).get();

        BigDecimal recyclableWaste = BigDecimal.valueOf(calculation.getSolidWasteTotal())
                .multiply(BigDecimal.valueOf(calculation.getRecyclePercentage()))
                .multiply(BigDecimal.valueOf(solidWasteFactor.getRecyclableFactor()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal nonRecyclableWaste = BigDecimal.valueOf(calculation.getSolidWasteTotal())
                .multiply(BigDecimal.valueOf(1 - calculation.getRecyclePercentage()))
                .multiply(BigDecimal.valueOf(solidWasteFactor.getNonRecyclableFactor()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal solidWasteEmissions = recyclableWaste.add(nonRecyclableWaste)
                .setScale(2, RoundingMode.HALF_UP);

        return solidWasteEmissions;
    }

    private BigDecimal calculateEnergyEmissions(Calculation calculation) {

        EnergyEmissionFactor energyFactor = energyEmissionFactorRepository.findById(calculation.getUf()).get();

        BigDecimal energyEmissions = BigDecimal.valueOf(calculation.getEnergyConsumption())
                .multiply(BigDecimal.valueOf(energyFactor.getFactor()))
                .setScale(2, RoundingMode.HALF_UP);

        return energyEmissions;
    }

    private Calculation findCalculationById(String id) {

        log.info("Searching for calculation with id [{}]...", id);

        Optional<Calculation> calculationOptional = calculationRepository.findById(id);

        if (!calculationOptional.isPresent()) {
            log.error("Calculation with id [{}] not found.", id);
            throw new CalculationNotFoundException(id);
        }

        log.info("Calculation with id [{}] found.", id);

        return calculationOptional.get();
    }

    private boolean saveCalculation(Calculation calculation) {

        log.info("Saving calculation with id [{}]...", calculation.getId());

        Calculation savedCalculation = calculationRepository.save(calculation);
        boolean success = savedCalculation != null;

        log.info("Calculation with id [{}] saved successfully.", calculation.getId());

        return success;
    }

}
