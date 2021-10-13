package org.alkemy.challenge.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.repositories.AnimatedCharacterRepository;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.alkemy.challenge.repositories.ProductionRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnimatedCharacterService {

    @Autowired
    private AnimatedCharacterRepository acRepo;

    @Autowired
    private ProductionRepository prodRepo;

    @Autowired
    private PhotoService photoServ;

    @Transactional
    public AnimatedCharacter create(AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            throw new ServiceException("Animated Character null");
        }
        if (ac.getName() == null || ac.getName().isEmpty()) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        if (ac.getId() != null && acRepo.findById(ac.getId()).isPresent()) {
            if (get(ac.getId()) == ac) {
                throw new ServiceException("Animated Character already exists");
            }
            throw new ServiceException("Animated Character ID already exists");
        }
        Photo image = ac.getImage();
        if (image != null) {
            if (ac.getId() == null) {
                ac.setImage(photoServ.create(image));
            } else if ((photoServ.get(ac.getId()) != image) && !photoServ.get(ac.getId()).equals(image)) {
                throw new ServiceException("Photo ID already exists");
            }
        }
        return acRepo.save(ac);
    }

    @Transactional
    public AnimatedCharacter create(MultipartFile file, String name, Integer age, Integer weight, String lore, Set<Production> productions) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore, productions);
        return create(ac);
    }

    @Transactional
    public AnimatedCharacter create(Photo image, String name, Integer age, Integer weight, String lore, Set<Production> productions) throws ServiceException {
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore, productions);
        return acRepo.save(ac);
    }

    public List<AnimatedCharacter> getAll() {
        return acRepo.findAll();
    }

    public AnimatedCharacter get(int id) throws ServiceException {
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    public List<AnimatedCharacter> getByName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByNameIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByNameContainingIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByProductionName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Production title cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByProductionNameIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByProductionNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Production title cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByProductionNameContainingIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByFilmName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Film title cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByFilmNameIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByFilmNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Film title cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByFilmNameContainingIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByShowName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Show title cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByShowNameIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByShowNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Show title cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByShowNameContainingIgnoreCase(name);
        return characters;
    }

    public List<AnimatedCharacter> getByProduction(int id) {
        List<AnimatedCharacter> characters = acRepo.findByProductionId(id);
        return characters;
    }

    public List<AnimatedCharacter> getByProduction(Production p) throws ServiceException {
        if (p == null || p.getId() == null) {
            throw new ServiceException("Production invalid or null");
        }
        List<AnimatedCharacter> characters = acRepo.findByProductionId(p.getId());
        return characters;
    }

    @Transactional
    public AnimatedCharacter update(int id, Photo image, String name, Integer age, Integer weight, String lore, Set<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        if (weight != null && weight <= 0) {
            throw new ServiceException("Animated Character weight has to be a positive number");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setName(name);
            ac.setAge(age);
            ac.setWeight(weight);
            ac.setLore(lore);
            image = photoServ.createIfNotExists(image);
            ac.setImage(image);

            Set<Production> prevProductions = ac.getAssociateProductions();
            if (productions != prevProductions && !prevProductions.equals(productions)) {
                if (productions == null) {
                    prevProductions.forEach(p -> {
                        p.getCast().remove(ac);
                    });
                    prodRepo.saveAll(prevProductions);
                } else {
                    productions.forEach(p -> {
                        if(!prevProductions.contains(p)){
                            p.getCast().add(ac);
                            prodRepo.save(p);
                        }
                    });
                    prevProductions.forEach(p -> {
                        if (!productions.contains(p)) {
                            p.getCast().remove(ac);
                            prodRepo.save(p);
                        }
                    });
                }
                ac.setAssociateProductions(productions);
            }
            return acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    @Transactional
    public AnimatedCharacter update(int id, MultipartFile file, String name, int age, int weight, String lore, Set<Production> productions) throws ServiceException {
        return update(id, new Photo(file), name, age, weight, lore, productions);
    }

    @Transactional
    public AnimatedCharacter update(int id, AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            throw new ServiceException("Animated Character cannot be null");
        }
        return update(id, ac.getImage(), ac.getName(), ac.getAge(), ac.getWeight(), ac.getLore(), ac.getAssociateProductions());
    }

    @Transactional
    public AnimatedCharacter update(AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            throw new ServiceException("Animated Character cannot be null");
        }
        if (ac.getId() == null) {
            throw new ServiceException("Animated Character ID cannot be null");
        }
        return update(ac.getId(), ac.getImage(), ac.getName(), ac.getAge(), ac.getWeight(), ac.getLore(), ac.getAssociateProductions());
    }

    @Transactional
    public void shutDown(int id) throws ServiceException {
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setShutdown(new Date());
            acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    @Transactional
    public void reOpen(int id) throws ServiceException {
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setShutdown(null);
            acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

}
