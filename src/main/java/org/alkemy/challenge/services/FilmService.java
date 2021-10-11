package org.alkemy.challenge.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public void create(Film f) throws ServiceException {
        if (f == null || f.getId() == null) {
            throw new ServiceException("Film null or has no ID");
        }
        if (filmRepo.findById(f.getId()).isPresent()) {
            throw new ServiceException("Film ID already exists");
        }
        photoServ.checkPhoto(f.getImage());
        filmRepo.save(f);
    }

    @Transactional
    public void create(MultipartFile file, String title, Date creation, Stars stars, List<AnimatedCharacter> cast, List<Category> categories) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Film title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Film creation date cannot be null");
        }
        Photo image = new Photo(file);
        photoServ.checkPhoto(image);
        Film f = new Film(image, title, creation, stars, cast, categories);
        filmRepo.save(f);
    }

    @Transactional
    public void create(Photo image, String title, Date creation, Stars stars, List<AnimatedCharacter> cast, List<Category> categories) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Film title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Film creation date cannot be null");
        }
        photoServ.checkPhoto(image);
        Film f = new Film(image, title, creation, stars, cast, categories);
        filmRepo.save(f);
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
    public void update(int id, MultipartFile file, String title, Date creation, Stars stars, List<AnimatedCharacter> cast) throws ServiceException {
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
            photoServ.checkPhoto(image);
            f.setImage(image);
            
            filmRepo.save(f);
        } else {
            throw new ServiceException("Film not found");
        }
    }

    @Transactional
    public void update(int id, Photo image, String title, Date creation, Stars stars, List<AnimatedCharacter> cast) throws ServiceException {
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
            
            photoServ.checkPhoto(image);
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
            
            photoServ.checkPhoto(updatedFilm.getImage());
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
            
            photoServ.checkPhoto(updatedFilm.getImage());
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

    public void addCharacter(int filmId, int characterId) throws ServiceException{
        Optional<Film> opt = filmRepo.findById(filmId);
        //TERMINAR PARA AÑADIR UN PERSONAJE A LA MUVI
    }
    
}
