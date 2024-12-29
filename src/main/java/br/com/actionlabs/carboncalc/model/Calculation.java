package br.com.actionlabs.carboncalc.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.actionlabs.carboncalc.dto.TransportationDTO;
import lombok.Data;

@Data
@Document("calculation")
public class Calculation {
    @Id
    private String id;
    private String name;
    private String email;
    private String uf;
    private String phoneNumber;
    private int energyConsumption;
    private List<TransportationDTO> transportation = new ArrayList<>();
    private int solidWasteTotal;
    private double recyclePercentage;
}
