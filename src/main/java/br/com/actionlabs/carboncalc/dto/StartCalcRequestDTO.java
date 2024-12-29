package br.com.actionlabs.carboncalc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StartCalcRequestDTO {
  @NotBlank(message = "Name required.")
  private String name;
  @NotBlank(message = "Email required.")
  @Email(message = "Invalid email format.")
  private String email;
  @NotBlank(message = "UF required.")
  private String uf;
  @NotBlank(message = "Phone number required.")
  private String phoneNumber;
}
