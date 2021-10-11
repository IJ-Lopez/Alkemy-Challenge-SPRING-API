package org.alkemy.challenge.services;

import java.util.List;
import java.util.Optional;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.repositories.ProductionRepository;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionService {

    @Autowired
    private ProductionRepository productionRepo;

    public List<Production> getAll() {
        return productionRepo.findAll();
    }

    public Production getById(int id) throws ServiceException {
        Optional<Production> opt = productionRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("Production not found");
        }
    }
}
