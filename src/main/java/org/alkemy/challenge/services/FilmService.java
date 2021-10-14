package org.alkemy.challenge.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Film;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.entities.enumerations.Stars;
import org.alkemy.challenge.repositories.CategoryRepository;
import org.alkemy.challenge.repositories.FilmRepository;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepo;

    @Autowired
    private PhotoService photoServ;

    @Autowired
    private AnimatedCharacterService acServ;
    
    @Autowired
    private CategoryRepository catRepo;

    @Transactional
    public Film create(Film f) throws ServiceException {
        if (f == null) {
            throw new ServiceException("Film null");
        }
        if (isSaved(f)){
            throw new ServiceException("Film already exists");
        }
        if (f.getTitle() == null || f.getTitle().isEmpty()) {
            throw new ServiceException("Film movie cannot be null");
        }
        f.setImage(photoServ.createIfNotExists(f.getImage()));
        if (f.getCast() != null && !f.getCast().isEmpty()){
            Set<AnimatedCharacter> cast = f.getCast();
            for(AnimatedCharacter ac : cast){
                if(ac != null){
                    if(!acServ.isSaved(ac)){
                        
                    }
                }
            }
        }
        return filmRepo.save(f);
    }

    @Transactional
    public Film create(MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = new Photo(file);
        Film f = new Film(image, title, creation, stars, cast, categories);
        return create(f);
    }

    @Transactional
    public Film create(Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Film f = new Film(image, title, creation, stars, cast, categories);
        return create(f);
    }

    @Transactional
    public Film createIfNotExists(Film f) throws ServiceException {
        if (f != null) {
            if (f.getId() != null && filmRepo.findById(f.getId()).isPresent()) {
                if (get(f.getId()) == f) {
                    return f;
                }
                throw new ServiceException("Film ID already exists");
            }
            if (f.getTitle() == null || f.getTitle().isEmpty()) {
                throw new ServiceException("Film movie cannot be null");
            }
            Photo image = f.getImage();
            if (image != null) {
                if (f.getId() == null) {
                    f.setImage(photoServ.create(image));
                } else if ((photoServ.get(f.getId()) != image) && !photoServ.get(f.getId()).equals(image)) {
                    throw new ServiceException("Photo ID already exists");
                }
            }
            return filmRepo.save(f);
        }
        return f;
    }

    @Transactional
    public Film createIfNotExists(MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = new Photo(file);
        Film f = new Film(image, title, creation, stars, cast, categories);
        return createIfNotExists(f);
    }

    @Transactional
    public Film createIfNotExists(Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Film f = new Film(image, title, creation, stars, cast, categories);
        return createIfNotExists(f);
    }

    public List<Film> getAll() {
        return filmRepo.findAll();
    }

    public Film get(int id) throws ServiceException {
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("Film not found");
        }
    }

    public List<Film> getByTitle(String title) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Film title cannot be null");
        }
        return filmRepo.findByTitleIgnoreCase(title);
    }

    public List<Film> getByNameLike(String title) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Film title cannot be null");
        }
        return filmRepo.findByTitleContainingIgnoreCase(title);
    }

    public List<Film> getByCharacterName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        return filmRepo.findByCharacterNameIgnoreCase(name);
    }

    public List<Film> getByCharacterNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        return filmRepo.findByCharacterNameContainingIgnoreCase(name);
    }

    public List<Film> getByCharacter(int id) {
        return filmRepo.findByCharacterId(id);
    }

    public List<Film> getByCharacter(AnimatedCharacter c) throws ServiceException {
        if (c == null || c.getId() == null) {
            throw new ServiceException("Animated Character invalid or null");
        }
        return filmRepo.findByCharacterId(c.getId());
    }

    @Transactional
    public void update(int id, Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Film title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Film creation date cannot be null");
        }
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setTitle(title);
            f.setCreation(creation);
            f.setStars(stars);
            f.setCast(cast);

            Set<Category> prevCategories = f.getCategories();
            if (categories != prevCategories && !prevCategories.equals(categories)) {
                if (categories == null || categories.isEmpty()) {
                    prevCategories.forEach(c -> {
                        c.getProductions().remove(f);
                    });
                    catRepo.saveAll(prevCategories);
                } else {
                    categories.forEach(c -> {
                        if (!prevCategories.contains(c)) {
                            c.getProductions().remove(f);
                            catRepo.save(c);
                        }
                    });
                    prevCategories.forEach(c -> {
                        if (!categories.contains(c)) {
                            c.getProductions().add(f);
                            catRepo.save(c);
                        }
                    });
                }
                f.setCategories(categories);
            }
            image = photoServ.createIfNotExists(image);
            f.setImage(image);

            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void update(int id, MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Film title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Film creation date cannot be null");
        }
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setTitle(title);
            f.setCreation(creation);
            f.setStars(stars);
            f.setCast(cast);

            Photo image = new Photo(file);
            photoServ.create(image);
            f.setImage(image);

            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void update(int id, Film updatedFilm) throws ServiceException {
        if (updatedFilm == null) {
            throw new ServiceException("Film cannot be null");
        }
        if (updatedFilm.getTitle() == null) {
            throw new ServiceException("Film title cannot be null");
        }
        if (updatedFilm.getCreation() == null) {
            throw new ServiceException("Film creation date cannot be null");
        }
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setTitle(updatedFilm.getTitle());
            f.setCreation(updatedFilm.getCreation());
            f.setStars(updatedFilm.getStars());
            f.setCast(updatedFilm.getCast());

            photoServ.create(updatedFilm.getImage());
            f.setImage(updatedFilm.getImage());

            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void update(Film updatedFilm) throws ServiceException {
        if (updatedFilm == null) {
            throw new ServiceException("Film cannot be null");
        }
        if (updatedFilm.getId() == null) {
            throw new ServiceException("Film ID cannot be null");
        }
        if (updatedFilm.getTitle() == null) {
            throw new ServiceException("Film title cannot be null");
        }
        if (updatedFilm.getCreation() == null) {
            throw new ServiceException("Film creation date cannot be null");
        }
        Optional<Film> opt = filmRepo.findById(updatedFilm.getId());
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setTitle(updatedFilm.getTitle());
            f.setCreation(updatedFilm.getCreation());
            f.setStars(updatedFilm.getStars());
            f.setCast(updatedFilm.getCast());

            photoServ.create(updatedFilm.getImage());
            f.setImage(updatedFilm.getImage());

            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void shutDown(int id) throws ServiceException {
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setShutdown(new Date());
            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void reOpen(int id) throws ServiceException {
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setShutdown(null);
            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    public void addCharacter(int filmId, int characterId) throws ServiceException {
        Optional<Film> opt = filmRepo.findById(filmId);
        //TERMINAR PARA AÑADIR UN PERSONAJE A LA MUVI
    }

    private boolean isSaved(Film f) throws ServiceException{
        if (f.getId() != null && filmRepo.findById(f.getId()).isPresent()) {
            if (get(f.getId()) != f) {
                throw new ServiceException("Film ID already exists");
            }
            return true;
        }
        return false;
    }
    
}
