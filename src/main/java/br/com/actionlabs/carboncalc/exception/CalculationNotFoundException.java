package br.com.actionlabs.carboncalc.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculationNotFoundException extends RuntimeException {

    public CalculationNotFoundException(String id) {
        super(String.format("Calculation with id [%s] could not be found.", id));
        log.error("Calculation with id [{}] not found.", id);
    }

}
