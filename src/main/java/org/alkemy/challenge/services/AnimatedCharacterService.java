package org.alkemy.challenge.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        if (ac.getId() != null && acRepo.findById(ac.getId()).isPresent()) {
            if (get(ac.getId()) == ac) {
                throw new ServiceException("Animated Character already exists");
            }
            throw new ServiceException("Animated Character ID already exists");
        }
        Photo image = ac.getImage();
        photoServ.checkPhoto(image);
        return acRepo.save(ac);
    }

    @Transactional
    public AnimatedCharacter create(MultipartFile file, String name, int age, int weight, String lore, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Name cannot be null");
        }
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
            photoServ.create(image);
        }
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore, productions);
        return acRepo.save(ac);
    }

    @Transactional
    public AnimatedCharacter create(Photo image, String name, int age, int weight, String lore, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Name cannot be null");
        }
        if (image != null) {
            if (image.getId() == null) {
                throw new ServiceException("Photo ID cannot be null");
            }
            if (photoServ.get(image.getId()) == null) {
                photoServ.create(image);
            } else if (photoServ.get(image.getId()) != image) {
                throw new ServiceException("Photo ID already exists");
            }
        }
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
    public void update(int id, MultipartFile file, String name, int age, int weight, String lore, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setName(name);
            ac.setAge(age);
            ac.setWeight(weight);
            ac.setLore(lore);

            Photo image = new Photo(file);
            photoServ.checkPhoto(image);
            ac.setImage(image);

            List<Production> prevProductions = ac.getAssociateProductions();
            if (!Objects.equals(productions, prevProductions)) {
                if (productions == null) {
                    prevProductions.forEach(p -> {
                        p.getCast().remove(ac);
                    });
                    prodRepo.saveAll(prevProductions);

                } else {
                    prevProductions.forEach(p -> {
                        if (!productions.contains(p)) {
                            p.getCast().remove(ac);
                            prodRepo.save(p);
                        }
                    });
                }
                ac.setAssociateProductions(productions);

            }
            acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    @Transactional
    public void update(int id, Photo image, String name, int age, int weight, String lore, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setName(name);
            ac.setAge(age);
            ac.setWeight(weight);
            ac.setLore(lore);

            photoServ.checkPhoto(image);
            ac.setImage(image);

            List<Production> prevProductions = ac.getAssociateProductions();
            if (!Objects.equals(productions, prevProductions)) {
                if (productions == null) {
                    prevProductions.forEach(p -> {
                        p.getCast().remove(ac);
                    });
                    prodRepo.saveAll(prevProductions);

                } else {
                    prevProductions.forEach(p -> {
                        if (!productions.contains(p)) {
                            p.getCast().remove(ac);
                            prodRepo.save(p);
                        }
                    });
                }
                ac.setAssociateProductions(productions);

            }
            acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    @Transactional
    public void update(int id, AnimatedCharacter updatedCharacter) throws ServiceException {
        if (updatedCharacter == null) {
            throw new ServiceException("Animated Character cannot be null");
        }
        if (updatedCharacter.getName() == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(id);
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setName(updatedCharacter.getName());
            ac.setAge(updatedCharacter.getAge());
            ac.setWeight(updatedCharacter.getWeight());
            ac.setLore(updatedCharacter.getLore());

            Photo image = updatedCharacter.getImage();
            photoServ.checkPhoto(image);
            ac.setImage(updatedCharacter.getImage());

            List<Production> prevProductions = ac.getAssociateProductions();
            if (!Objects.equals(updatedCharacter.getAssociateProductions(), prevProductions)) {
                if (updatedCharacter.getAssociateProductions() == null) {
                    prevProductions.forEach(p -> {
                        p.getCast().remove(ac);
                    });
                    prodRepo.saveAll(prevProductions);

                } else {
                    prevProductions.forEach(p -> {
                        if (!updatedCharacter.getAssociateProductions().contains(p)) {
                            p.getCast().remove(ac);
                            prodRepo.save(p);
                        }
                    });
                }
                ac.setAssociateProductions(updatedCharacter.getAssociateProductions());
            }
            acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
    }

    @Transactional
    public void update(AnimatedCharacter updatedCharacter) throws ServiceException {
        if (updatedCharacter == null) {
            throw new ServiceException("Animated Character cannot be null");
        }
        if (updatedCharacter.getId() == null) {
            throw new ServiceException("Animated Character ID cannot be null");
        }
        if (updatedCharacter.getName() == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        Optional<AnimatedCharacter> opt = acRepo.findById(updatedCharacter.getId());
        if (opt.isPresent()) {
            AnimatedCharacter ac = opt.get();
            ac.setName(updatedCharacter.getName());
            ac.setAge(updatedCharacter.getAge());
            ac.setWeight(updatedCharacter.getWeight());
            ac.setLore(updatedCharacter.getLore());

            Photo image = updatedCharacter.getImage();
            photoServ.checkPhoto(image);
            ac.setImage(image);

            List<Production> prevProductions = ac.getAssociateProductions();
            if (!Objects.equals(updatedCharacter.getAssociateProductions(), prevProductions)) {
                if (updatedCharacter.getAssociateProductions() == null) {
                    prevProductions.forEach(p -> {
                        p.getCast().remove(ac);
                    });
                    prodRepo.saveAll(prevProductions);

                } else {
                    prevProductions.forEach(p -> {
                        if (!updatedCharacter.getAssociateProductions().contains(p)) {
                            p.getCast().remove(ac);
                            prodRepo.save(p);
                        }
                    });

                }
                ac.setAssociateProductions(updatedCharacter.getAssociateProductions());
            }
            acRepo.save(ac);
        } else {
            throw new ServiceException("Animated Character not found");
        }
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
