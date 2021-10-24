package org.alkemy.challenge.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Show;
import org.alkemy.challenge.entities.enumerations.Stars;
import org.alkemy.challenge.repositories.ShowRepository;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepo;

    @Autowired
    private PhotoService photoServ;

    @Autowired
    private AnimatedCharacterService acServ;

    @Autowired
    private CategoryService catServ;

    @Transactional
    public Show forceCreate(Show s) throws ServiceException {
        if (s == null) {
            s = new Show();
        }
        s.setCast(saveCast(s.getCast()));
        s.setCategories(saveCategories(s.getCategories()));
        s.setImage(photoServ.createIfNotExists(s.getImage()));
        return showRepo.save(s);
    }

    @Transactional
    public Show forceCreate(Integer id, Show s) throws ServiceException {
        s.setId(id);
        return forceCreate(s);
    }

    @Transactional
    public Show create(Show s) throws ServiceException {
        if (s == null) {
            throw new ServiceException("Show null");
        }
        if (isSaved(s)) {
            throw new ServiceException("Show already exists");
        }
        if (s.getTitle() == null || s.getTitle().isEmpty()) {
            throw new ServiceException("Show title cannot be null");
        }
        s.setCast(saveCast(s.getCast()));
        s.setCategories(saveCategories(s.getCategories()));
        s.setImage(photoServ.createIfNotExists(s.getImage()));
        return showRepo.save(s);
    }

    @Transactional
    public Show create(MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
        Show s = new Show(image, title, creation, stars, cast, categories);
        return create(s);
    }

    @Transactional
    public Show create(Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Show s = new Show(image, title, creation, stars, cast, categories);
        return create(s);
    }

    @Transactional
    public Show createIfNotExists(Show s) throws ServiceException {
        if (!isSaved(s)) {
            s = create(s);
        }
        return s;
    }

    @Transactional
    public Show createIfNotExists(MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
        Show s = new Show(image, title, creation, stars, cast, categories);
        return createIfNotExists(s);
    }

    @Transactional
    public Show createIfNotExists(Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Show s = new Show(image, title, creation, stars, cast, categories);
        return createIfNotExists(s);
    }

    public List<Show> getAll() {
        return showRepo.findAll();
    }

    public Show get(int id) throws ServiceException {
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("Show not found");
        }
    }

    public List<Show> getByTitle(String title) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Show title cannot be null");
        }
        return showRepo.findByTitleIgnoreCase(title);
    }

    public List<Show> getByTitleLike(String title) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Show title cannot be null");
        }
        return showRepo.findByTitleContainingIgnoreCase(title);
    }

    public List<Show> getByCharacterName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        return showRepo.findByCharacterNameIgnoreCase(name);
    }

    public List<Show> getByCharacterNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Animated Character name cannot be null");
        }
        return showRepo.findByCharacterNameContainingIgnoreCase(name);
    }

    public List<Show> getByCharacter(int id) {
        return showRepo.findByCharacterId(id);
    }

    public List<Show> getByCharacter(AnimatedCharacter c) throws ServiceException {
        if (c == null || c.getId() == null) {
            throw new ServiceException("Animated Character invalid or null");
        }
        return showRepo.findByCharacterId(c.getId());
    }
    
    public List<Show> getByCategory(int id) {
        return showRepo.findByCategoryId(id);
    }

    @Transactional
    public Show update(Integer id, Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setTitle(title);
            s.setCast(saveCast(cast));
            s.setCategories(saveCategories(categories));
            image = photoServ.createIfNotExists(image);
            s.setImage(image);
            s.setCreation(creation);
            s.setStars(stars);
            return showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public Show update(Integer id, MultipartFile file, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) throws ServiceException {
        Photo image = null;
        if (file != null) {
            image = new Photo(file);
        }
        return update(id, image, title, creation, stars, cast, categories);
    }

    @Transactional
    public Show update(Integer id, Show updatedShow) throws ServiceException {
        return update(id, updatedShow.getImage(), updatedShow.getTitle(), updatedShow.getCreation(), updatedShow.getStars(), updatedShow.getCast(), updatedShow.getCategories());
    }

    @Transactional
    public Show update(Show updatedShow) throws ServiceException {
        return update(updatedShow.getId(), updatedShow.getImage(), updatedShow.getTitle(), updatedShow.getCreation(), updatedShow.getStars(), updatedShow.getCast(), updatedShow.getCategories());
    }

    @Transactional
    public void shutDown(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setShutdown(new Date());
            showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void reOpen(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setShutdown(null);
            showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void delete(Integer id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("ID cannot be null");
        }
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            showRepo.delete(opt.get());
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void delete(Show s) throws ServiceException {
        if (s == null) {
            throw new ServiceException("Show cannot be null");
        }
        delete(s.getId());
    }

    @Transactional
    public void addCharacter(int showId, int characterId) throws ServiceException {
        AnimatedCharacter ac = acServ.get(characterId);
        Optional<Show> optShow = showRepo.findById(showId);
        if (optShow.isPresent()) {
            Show show = optShow.get();
            if (show.getCast().add(ac)) {
                throw new ServiceException("Animated Character was already part of the cast");
            }
            showRepo.save(show);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void removeCharacter(int showId, int characterId) throws ServiceException {
        AnimatedCharacter ac = acServ.get(characterId);
        Optional<Show> optShow = showRepo.findById(showId);
        if (optShow.isPresent()) {
            Show show = optShow.get();
            if (!show.getCast().remove(ac)) {
                throw new ServiceException("Animated Character was not part of the cast");
            }
            showRepo.save(show);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void addCategory(int showId, int categoryId) throws ServiceException {
        Category c = catServ.get(categoryId);
        Optional<Show> optShow = showRepo.findById(showId);
        if (optShow.isPresent()) {
            Show show = optShow.get();
            if (!show.getCategories().add(c)) {
                throw new ServiceException("Category was already part of the cast");
            }
            showRepo.save(show);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void addCategory(int showId, String category) throws ServiceException {
        List<Category> dbCategories = catServ.getByName(category);
        Category c = !dbCategories.isEmpty() ? dbCategories.get(0) : new Category(category, null);
        Optional<Show> optShow = showRepo.findById(showId);
        if (optShow.isPresent()) {
            Show show = optShow.get();
            if (!show.getCategories().add(c)) {
                throw new ServiceException("Category was already part of the cast");
            }
            showRepo.save(show);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void removeCategory(int showId, int categoryId) throws ServiceException {
        Category c = catServ.get(categoryId);
        Optional<Show> optShow = showRepo.findById(showId);
        if (optShow.isPresent()) {
            Show show = optShow.get();
            if (!show.getCategories().remove(c)) {
                throw new ServiceException("Category was not part of the cast");
            }
            showRepo.save(show);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void removeCategory(int showId, String category) throws ServiceException {
        List<Category> dbCategories = catServ.getByName(category);
        if(dbCategories.isEmpty()){
            throw new ServiceException("Category does not exists");
        }
        Category c = dbCategories.get(0);
        Optional<Show> optShow = showRepo.findById(showId);
        if (optShow.isPresent()) {
            Show show = optShow.get();
            if (!show.getCategories().remove(c)) {
                throw new ServiceException("Category was not part of the cast");
            }
            showRepo.save(show);
        } else {
            throw new ServiceException("Show not found");
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

    private boolean isSaved(Show s) throws ServiceException {
        if (s.getId() != null && showRepo.findById(s.getId()).isPresent()) {
            if (get(s.getId()) != s) {
                throw new ServiceException("Show ID already exists");
            }
            return true;
        }
        return false;
    }
}
