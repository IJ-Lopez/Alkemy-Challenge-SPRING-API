package org.alkemy.challenge.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.repositories.CategoryRepository;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private PhotoService photoServ;

    @Transactional
    public Category create(Category c) throws ServiceException {
        if (c == null) {
            throw new ServiceException("Category cannot be null");
        }
        if (c.getName() == null || c.getName().isEmpty()){
            throw new ServiceException("Category name cannot be null");
        }
        if (isSaved(c)) {
            throw new ServiceException("Category already exists");
        }
        c.setImage(photoServ.createIfNotExists(c.getImage()));
        return categoryRepo.save(c);
    }

    @Transactional
    public Category create(String name, MultipartFile file) throws ServiceException {
        Photo image = null;
        if(file != null){
            image = new Photo(file);
        }
        Category c = new Category(name, image);
        return create(c);
    }

    @Transactional
    public Category create(String name, Photo image) throws ServiceException {
        Category c = new Category(name, image);
        return create(c);
    }

    @Transactional
    public Category createIfNotExists(Category c) throws ServiceException {
        if(!isSaved(c)){
            c = create(c);
        }
        return c;
    }

    @Transactional
    public Category createIfNotExists(String name, MultipartFile file) throws ServiceException {
        Photo image = null;
        if(file != null){
            image = new Photo(file);
        }
        Category c = new Category(name, image);
        return createIfNotExists(c);
    }

    @Transactional
    public Category createIfNotExists(String name, Photo image) throws ServiceException {
        Category c = new Category(name, image);
        return createIfNotExists(c);
    }

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    public Category get(int id) throws ServiceException {
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("Category not found");
        }
    }

    public List<Category> getByName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Category name cannot be null");
        }
        List<Category> categories = categoryRepo.findByNameIgnoreCase(name);
        return categories;
    }

    public List<Category> getByNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Category name cannot be null");
        }
        List<Category> categories = categoryRepo.findByNameContainingIgnoreCase(name);
        return categories;
    }

    public List<Category> getByProductionName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Production title cannot be null");
        }
        List<Category> characters = categoryRepo.findByProductionNameIgnoreCase(name);
        return characters;
    }

    public List<Category> getByProductionNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Production title cannot be null");
        }
        List<Category> characters = categoryRepo.findByProductionNameContainingIgnoreCase(name);
        return characters;
    }

    public List<Category> getByFilmName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Film title cannot be null");
        }
        List<Category> characters = categoryRepo.findByFilmNameIgnoreCase(name);
        return characters;
    }

    public List<Category> getByFilmNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Film title cannot be null");
        }
        List<Category> characters = categoryRepo.findByFilmNameContainingIgnoreCase(name);
        return characters;
    }

    public List<Category> getByShowName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Show title cannot be null");
        }
        List<Category> characters = categoryRepo.findByShowNameIgnoreCase(name);
        return characters;
    }

    public List<Category> getByShowNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Show title cannot be null");
        }
        List<Category> characters = categoryRepo.findByShowNameContainingIgnoreCase(name);
        return characters;
    }

    public List<Category> getByProduction(int id) throws ServiceException {
        List<Category> characters = categoryRepo.findByProductionId(id);
        return characters;
    }

    public List<Category> getByProduction(Production p) throws ServiceException {
        if (p == null || p.getId() == null) {
            throw new ServiceException("Production invalid or null");
        }
        List<Category> characters = categoryRepo.findByProductionId(p.getId());
        return characters;
    }
    
    @Transactional
    public Category update(Integer id, String name, Photo image) throws ServiceException {
        if(id == null){
            throw new ServiceException("ID cannot be null");
        }
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setName(name);
            c.setImage(photoServ.createIfNotExists(c.getImage()));
            c.setProductions(null);
            return categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }

    @Transactional
    public Category update(Integer id, String name, MultipartFile file) throws ServiceException {
        Photo image = null;
        if(file != null){
            image = new Photo(file);
        }
        return update(id, name, image);
    }

    @Transactional
    public Category update(Integer id, Category updatedCategory) throws ServiceException {
        return update(id, updatedCategory.getName(), updatedCategory.getImage());
    }

    @Transactional
    public Category update(Category updatedCategory) throws ServiceException {
        return update(updatedCategory.getId(), updatedCategory.getName(), updatedCategory.getImage());
    }

    @Transactional
    public void shutDown(Integer id) throws ServiceException {
        if(id == null){
            throw new ServiceException("ID cannot be null");
        }
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setShutdown(new Date());
            categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }

    @Transactional
    public void reOpen(Integer id) throws ServiceException {
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setShutdown(null);
            categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }

    private boolean isSaved(Category c) throws ServiceException {
        if (c.getId() != null && categoryRepo.findById(c.getId()).isPresent()) {
            if (get(c.getId()) != c && getByName(c.getName()).isEmpty()) {
                throw new ServiceException("Category ID already exists");
            }
            return true;
        }
        return false;
    }

}
