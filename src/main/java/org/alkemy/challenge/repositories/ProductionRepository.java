package org.alkemy.challenge.repositories;

import javax.transaction.Transactional;
import org.alkemy.challenge.entities.Production;

@Transactional
public interface ProductionRepository extends ProductionBaseRepository<Production>{
    
}
