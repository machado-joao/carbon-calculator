package br.com.actionlabs.carboncalc.dto;

import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class UpdateCalcInfoRequestDTO {
  private String id;
  private int energyConsumption;
  private List<TransportationDTO> transportation;
  private int solidWasteTotal;
  @DecimalMin(value = "0.0", inclusive = true, message = "Recycle percentage must be at least 0.0.")
  @DecimalMax(value = "1.0", inclusive = true, message = "Recycle percentage must be at most 1.0.")
  private double recyclePercentage;
}
