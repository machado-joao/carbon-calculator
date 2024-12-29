package br.com.actionlabs.carboncalc.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.actionlabs.carboncalc.enums.TransportationType;
import lombok.Data;

@Data
@Document("transportationEmissionFactor")
public class TransportationEmissionFactor {
  @Id
  private TransportationType type;
  private double factor;
}
