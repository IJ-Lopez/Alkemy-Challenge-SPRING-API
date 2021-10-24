package org.alkemy.challenge.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.repositories.AnimatedCharacterRepository;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnimatedCharacterService {

    @Autowired
    private AnimatedCharacterRepository acRepo;

    @Autowired
    private PhotoService photoServ;

    @Transactional
    public AnimatedCharacter forceCreate(AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            ac = new AnimatedCharacter();
        }
        Photo image = ac.getImage();
        if (image != null) {
            if ((photoServ.get(ac.getId()) != image) && !photoServ.get(ac.getId()).equals(image)) {
                image.setId(null);
            }
            ac.setImage(photoServ.create(image));
        }
        if(ac.getId() != null){
            acRepo.put(ac.getId());
        }
        return acRepo.save(ac);
    }

    @Transactional
    public AnimatedCharacter forceCreate(Integer id, AnimatedCharacter ac) throws ServiceException {
        ac.setId(id);
        return forceCreate(ac);
    }

    @Transactional
    public AnimatedCharacter create(AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            throw new ServiceException("Animated Character null");
        }
        if (ac.getName() == null || ac.getName().isEmpty()) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        if (ac.getWeight() != null && ac.getWeight() < 0) {
            throw new ServiceException("Animated Character weight has to be a positive number");
        }
        if (ac.getAge() != null && ac.getAge() < 0) {
            throw new ServiceException("Animated Character weight has to be a positive number");
        }
        if (isSaved(ac)) {
            throw new ServiceException("Animated Character already exists");
        }
        Photo image = ac.getImage();
        if (image != null) {
            if (ac.getId() == null) {
                ac.setImage(photoServ.create(image));
            } else if ((photoServ.get(ac.getId()) != image) && !photoServ.get(ac.getId()).equals(image)) {
                throw new ServiceException("Photo ID already exists");
            }
        }
        ac.setAssociateProductions(null);
        ac.setUpload(new Date());
        return acRepo.save(ac);
    }

    @Transactional
    public AnimatedCharacter create(MultipartFile file, String name, Integer age, Integer weight, String lore) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore);
        return create(ac);
    }

    @Transactional
    public AnimatedCharacter create(Photo image, String name, Integer age, Integer weight, String lore) throws ServiceException {
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore);
        return create(ac);
    }

    @Transactional
    public AnimatedCharacter createIfNotExists(AnimatedCharacter ac) throws ServiceException {
        if (!isSaved(ac)) {
            ac = create(ac);
        }
        return ac;
    }

    @Transactional
    public AnimatedCharacter createIfNotExists(MultipartFile file, String name, Integer age, Integer weight, String lore) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore);
        return createIfNotExists(ac);
    }

    @Transactional
    public AnimatedCharacter createIfNotExists(Photo image, String name, Integer age, Integer weight, String lore) throws ServiceException {
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore);
        return createIfNotExists(ac);
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
    
    public List<AnimatedCharacter> getByAge(Integer age) throws ServiceException {
        if (age == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        List<AnimatedCharacter> characters = acRepo.findByAge(age);
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
    public AnimatedCharacter update(Integer id, Photo image, String name, Integer age, Integer weight, String lore) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setName(name);
            ac.setAge(age);
            ac.setWeight(weight);
            ac.setLore(lore);
            ac.setImage(photoServ.createIfNotExists(image));
            ac.setAssociateProductions(null);
            return acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    @Transactional
    public AnimatedCharacter update(Integer id, MultipartFile file, String name, Integer age, Integer weight, String lore) throws ServiceException {
        return update(id, new Photo(file), name, age, weight, lore);
    }

    @Transactional
    public AnimatedCharacter update(Integer id, AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            throw new ServiceException("Animated Character cannot be null");
        }
        return update(id, ac.getImage(), ac.getName(), ac.getAge(), ac.getWeight(), ac.getLore());
    }

    @Transactional
    public AnimatedCharacter update(AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            throw new ServiceException("Animated Character cannot be null");
        }
        return update(ac.getId(), ac.getImage(), ac.getName(), ac.getAge(), ac.getWeight(), ac.getLore());
    }

    @Transactional
    public void delete(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            acRepo.delete(opt.get());
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    @Transactional
    public void delete(AnimatedCharacter ac) throws ServiceException {
        if (ac == null) {
            throw new ServiceException("Animated Character cannot be null");
        }
        delete(ac.getId());
    }

    @Transactional
    public void shutDown(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
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
    public void reOpen(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setShutdown(null);
            acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    boolean isSaved(AnimatedCharacter ac) throws ServiceException {
        if (ac.getId() != null && acRepo.findById(ac.getId()).isPresent()) {
            if (get(ac.getId()) != ac) {
                throw new ServiceException("Animated Character ID already exists");
            }
            return true;
        }
        return false;
    }

}
