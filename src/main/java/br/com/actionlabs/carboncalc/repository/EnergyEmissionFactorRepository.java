package br.com.actionlabs.carboncalc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.actionlabs.carboncalc.model.EnergyEmissionFactor;

@Repository
public interface EnergyEmissionFactorRepository extends MongoRepository<EnergyEmissionFactor, String> {

}
