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

    public Production get(int id) throws ServiceException {
        Optional<Production> opt = productionRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("Production not found");
        }
    }
    
    public List<Production> getByTitle(String title) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Production title cannot be null");
        }
        return productionRepo.findByTitleIgnoreCase(title);
    }

    public List<Production> getByTitleLike(String title) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Production title cannot be null");
        }
        return productionRepo.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Production> getByCategory(Integer id) throws ServiceException{
        if(id == null){
            throw new ServiceException("ID cannot be null");
        }
        return productionRepo.findByCategoryId(id);
    }
}
