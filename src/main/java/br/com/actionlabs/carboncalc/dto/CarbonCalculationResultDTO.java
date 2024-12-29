package br.com.actionlabs.carboncalc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarbonCalculationResultDTO {
  private double energy;
  private double transportation;
  private double solidWaste;
  private double total;
}
