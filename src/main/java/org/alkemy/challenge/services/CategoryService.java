package org.alkemy.challenge.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    public void create(Category c) throws ServiceException {
        if (c == null || c.getId() == null) {
            throw new ServiceException("Category null or has no ID");
        }
        if (categoryRepo.findById(c.getId()).isPresent()) {
            throw new ServiceException("Category ID already exists");
        }
        photoServ.checkPhoto(c.getImage());
        categoryRepo.save(c);
    }

    @Transactional
    public void create(String name, MultipartFile file, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Category name cannot be null");
        }
        Photo image = new Photo(file);
        photoServ.checkPhoto(image);
        Category c = new Category(name, image, productions);
        categoryRepo.save(c);
    }

    @Transactional
    public void create(String name, Photo image, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Category name cannot be null");
        }
        photoServ.checkPhoto(image);
        Category c = new Category(name, image, productions);
        categoryRepo.save(c);
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
    public void update(int id, String name, MultipartFile file, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Category name cannot be null");
        }
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setName(name);
            c.setProductions(productions);
            
            Photo image = new Photo(file);
            photoServ.checkPhoto(image);
            c.setImage(image);
            
            categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }
    
    @Transactional
    public void update(int id, String name, Photo image, List<Production> productions) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Category name cannot be null");
        }
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setName(name);
            c.setProductions(productions);
            
            photoServ.checkPhoto(image);
            c.setImage(image);
            
            categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }

    @Transactional
    public void update(int id, Category updatedCategory) throws ServiceException {
        if (updatedCategory == null) {
            throw new ServiceException("Category cannot be null");
        }
        if (updatedCategory.getName() == null) {
            throw new ServiceException("Category name cannot be null");
        }
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setName(c.getName());
            c.setProductions(c.getProductions());
            
            photoServ.checkPhoto(c.getImage());
            c.setImage(c.getImage());
            
            categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }

    @Transactional
    public void update(Category updatedCategory) throws ServiceException {
        if (updatedCategory == null) {
            throw new ServiceException("Category cannot be null");
        }
        if (updatedCategory.getId() == null) {
            throw new ServiceException("Category ID cannot be null");
        }
        if (updatedCategory.getName() == null) {
            throw new ServiceException("Category name cannot be null");
        }
        Optional<Category> opt = categoryRepo.findById(updatedCategory.getId());
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setName(c.getName());
            c.setProductions(c.getProductions());
            
            photoServ.checkPhoto(c.getImage());
            c.setImage(c.getImage());
            
            categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }

    @Transactional
    public void shutDown(int id) throws ServiceException {
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
    public void reOpen(int id) throws ServiceException {
        Optional<Category> opt = categoryRepo.findById(id);
        if (opt.isPresent()) {
            Category c = opt.get();
            c.setShutdown(null);
            categoryRepo.save(c);
        } else {
            throw new ServiceException("Category not found");
        }
    }

}
