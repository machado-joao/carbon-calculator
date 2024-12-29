package br.com.actionlabs.carboncalc.exception;

public class CalculationNotFoundException extends RuntimeException {

    public CalculationNotFoundException(String id) {
        super(String.format("Calculation with id [%s] could not be found.", id));
    }

}
