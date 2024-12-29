package br.com.actionlabs.carboncalc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.actionlabs.carboncalc.model.Calculation;

@Repository
public interface CalculationRepository extends MongoRepository<Calculation, String> {

}
