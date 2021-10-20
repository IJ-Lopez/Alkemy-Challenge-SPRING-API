package org.alkemy.challenge.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Film;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.enumerations.Stars;
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
    private CategoryService catServ;

    @Transactional
    public Film forceCreate(Film f) throws ServiceException {
        if (f == null) {
            f = new Film();
        }
        f.setCast(saveCast(f.getCast()));
        f.setCategories(saveCategories(f.getCategories()));
        f.setImage(photoServ.createIfNotExists(f.getImage()));
        if(f.getId() != null){
            filmRepo.put(f.getId());
        }
        return filmRepo.save(f);
    }

    @Transactional
    public Film forceCreate(Integer id, Film f) throws ServiceException {
        f.setId(id);
        return forceCreate(f);
    }

    @Transactional
    public Film create(Film f) throws ServiceException {
        if (f == null) {
            throw new ServiceException("Film null");
        }
        if (isSaved(f)) {
            throw new ServiceException("Film already exists");
        }
        if (f.getTitle() == null || f.getTitle().isEmpty()) {
            throw new ServiceException("Film title cannot be null");
        }
        f.setCast(saveCast(f.getCast()));
        f.setCategories(saveCategories(f.getCategories()));
        f.setImage(photoServ.createIfNotExists(f.getImage()));
        return filmRepo.save(f);
    }

    @Transactional
    public Film create(MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
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
        if (!isSaved(f)) {
            f = create(f);
        }
        return f;
    }

    @Transactional
    public Film createIfNotExists(MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
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

    public List<Film> getByCategory(int id) {
        return filmRepo.findByCategoryId(id);
    }

    @Transactional
    public Film update(Integer id, Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setTitle(title);
            f.setCast(saveCast(cast));
            f.setCategories(saveCategories(categories));
            image = photoServ.createIfNotExists(image);
            f.setImage(image);
            f.setCreation(creation);
            f.setStars(stars);
            return filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public Film update(Integer id, MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
        return update(id, image, title, creation, stars, cast, categories);
    }

    @Transactional
    public Film update(Integer id, Film updatedFilm) throws ServiceException {
        return update(id, updatedFilm.getImage(), updatedFilm.getTitle(), updatedFilm.getCreation(), updatedFilm.getStars(), updatedFilm.getCast(), updatedFilm.getCategories());
    }

    @Transactional
    public Film update(Film updatedFilm) throws ServiceException {
        return update(updatedFilm.getId(), updatedFilm.getImage(), updatedFilm.getTitle(), updatedFilm.getCreation(), updatedFilm.getStars(), updatedFilm.getCast(), updatedFilm.getCategories());
    }

    @Transactional
    public void shutDown(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
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
    public void reOpen(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            Film f = opt.get();
            f.setShutdown(null);
            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void delete(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<Film> opt = filmRepo.findById(id);
        if (opt.isPresent()) {
            filmRepo.delete(opt.get());
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void delete(Film f) throws ServiceException {
        if (f == null) {
            throw new ServiceException("Film cannot be null");
        }
        delete(f.getId());
    }

    @Transactional
    public void addCharacter(int filmId, int characterId) throws ServiceException {
        AnimatedCharacter ac = acServ.get(characterId);
        Optional<Film> optFilm = filmRepo.findById(filmId);
        if (optFilm.isPresent()) {
            Film film = optFilm.get();
            if(!film.getCast().add(ac)){
                throw new ServiceException("Animated Character was already part of the cast");
            }
            filmRepo.save(film);
        } else {
            throw new ServiceException("Film not found");
        }
    }
    
    @Transactional
    public void removeCharacter(int filmId, int characterId) throws ServiceException {
        AnimatedCharacter ac = acServ.get(characterId);
        Optional<Film> optFilm = filmRepo.findById(filmId);
        if (optFilm.isPresent()) {
            Film film = optFilm.get();
            if(!film.getCast().remove(ac)){
                throw new ServiceException("Animated Character was not part of the cast");
            }
            filmRepo.save(film);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void addCategory(int filmId, int categoryId) throws ServiceException {
        Category c = catServ.get(categoryId);
        Optional<Film> optFilm = filmRepo.findById(filmId);
        if (optFilm.isPresent()) {
            Film film = optFilm.get();
            if(!film.getCategories().add(c)){
                throw new ServiceException("Category was already part of the cast");
            }
            filmRepo.save(film);
        } else {
            throw new ServiceException("Film not found");
        }
    }
    
    @Transactional
    public void addCategory(int filmId, String category) throws ServiceException {
        List<Category> dbCategories = catServ.getByName(category);
        Category c = !dbCategories.isEmpty() ? dbCategories.get(0) : new Category(category, null);
        Optional<Film> optFilm = filmRepo.findById(filmId);
        if (optFilm.isPresent()) {
            Film film = optFilm.get();
            if (!film.getCategories().add(c)) {
                throw new ServiceException("Category was already part of the cast");
            }
            filmRepo.save(film);
        } else {
            throw new ServiceException("Film not found");
        }
    }
    
    @Transactional
    public void removeCategory(int filmId, int categoryId) throws ServiceException {
        Category c = catServ.get(categoryId);
        Optional<Film> optFilm = filmRepo.findById(filmId);
        if (optFilm.isPresent()) {
            Film film = optFilm.get();
            if(!film.getCategories().remove(c)){
                throw new ServiceException("Category was not part of the cast");
            }
            filmRepo.save(film);
        } else {
            throw new ServiceException("Film not found");
        }
    }
    
    @Transactional
    public void removeCategory(int filmId, String category) throws ServiceException {
        List<Category> dbCategories = catServ.getByName(category);
        if(dbCategories.isEmpty()){
            throw new ServiceException("Category does not exists");
        }
        Category c = dbCategories.get(0);
        Optional<Film> optFilm = filmRepo.findById(filmId);
        if (optFilm.isPresent()) {
            Film film = optFilm.get();
            if (!film.getCategories().remove(c)) {
                throw new ServiceException("Category was not part of the cast");
            }
            filmRepo.save(film);
        } else {
            throw new ServiceException("Film not found");
        }
    }


    @Transactional
    private Set<AnimatedCharacter> saveCast(Set<AnimatedCharacter> cast) throws ServiceException {
        if (cast != null && cast.isEmpty()) {
            Set<AnimatedCharacter> savedCast = new HashSet();
            for (AnimatedCharacter ac : cast) {
                if (ac != null) {
                    savedCast.add(acServ.createIfNotExists(ac));
                }
            }
            return savedCast;
        }
        return cast;
    }

    @Transactional
    private Set<Category> saveCategories(Set<Category> categories) throws ServiceException {
        if (categories != null && categories.isEmpty()) {
            Set<Category> savedCategories = new HashSet();
            for (Category c : categories) {
                if (c != null) {
                    savedCategories.add(catServ.createIfNotExists(c));
                }
            }
            return savedCategories;
        }
        return categories;
    }

    private boolean isSaved(Film f) throws ServiceException {
        if (f.getId() != null && filmRepo.findById(f.getId()).isPresent()) {
            if (get(f.getId()) != f) {
                throw new ServiceException("Film ID already exists");
            }
            return true;
        }
        return false;
    }
}
