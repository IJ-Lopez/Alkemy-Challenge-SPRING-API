/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alkemy.challenge.services;

import java.util.Date;
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

    @Transactional
    public void create(Show s) throws ServiceException {
        if (s == null || s.getId() == null) {
            throw new ServiceException("Show null or has no ID");
        }
        if (showRepo.findById(s.getId()).isPresent()) {
            throw new ServiceException("Show ID already exists");
        }
        photoServ.checkPhoto(s.getImage());
        showRepo.save(s);
    }

    @Transactional
    public void create(MultipartFile file, String title, Date creation, Stars stars, List<AnimatedCharacter> cast, List<Category> categories) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Show title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Show creation date cannot be null");
        }
        Photo image = new Photo(file);
        photoServ.checkPhoto(image);
        Show s = new Show(image, title, creation, stars, cast, categories);
        showRepo.save(s);
    }

    @Transactional
    public void create(Photo image, String title, Date creation, Stars stars, List<AnimatedCharacter> cast, List<Category> categories) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Show title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Show creation date cannot be null");
        }
        photoServ.checkPhoto(image);
        Show s = new Show(image, title, creation, stars, cast, categories);
        showRepo.save(s);
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

    public List<Show> getByNameLike(String title) throws ServiceException {
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

    @Transactional
    public void update(int id, MultipartFile file, String title, Date creation, Stars stars, List<AnimatedCharacter> cast) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Show title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Show creation date cannot be null");
        }
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setTitle(title);
            s.setCreation(creation);
            s.setStars(stars);
            s.setCast(cast);
            
            Photo image = new Photo(file);
            photoServ.checkPhoto(image);
            s.setImage(image);
            
            showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void update(int id, Photo image, String title, Date creation, Stars stars, List<AnimatedCharacter> cast) throws ServiceException {
        if (title == null) {
            throw new ServiceException("Show title cannot be null");
        }
        if (creation == null) {
            throw new ServiceException("Show creation date cannot be null");
        }
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setTitle(title);
            s.setCreation(creation);
            s.setStars(stars);
            s.setCast(cast);
            
            photoServ.checkPhoto(image);
            s.setImage(image);
            
            showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void update(int id, Show updatedShow) throws ServiceException {
        if (updatedShow == null) {
            throw new ServiceException("Show cannot be null");
        }
        if (updatedShow.getTitle() == null) {
            throw new ServiceException("Show title cannot be null");
        }
        if (updatedShow.getCreation() == null) {
            throw new ServiceException("Show creation date cannot be null");
        }
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setTitle(updatedShow.getTitle());
            s.setCreation(updatedShow.getCreation());
            s.setStars(updatedShow.getStars());
            s.setCast(updatedShow.getCast());
            
            photoServ.checkPhoto(updatedShow.getImage());
            s.setImage(updatedShow.getImage());
            
            showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void update(Show updatedShow) throws ServiceException {
        if (updatedShow == null) {
            throw new ServiceException("Show cannot be null");
        }
        if (updatedShow.getId() == null) {
            throw new ServiceException("Show ID cannot be null");
        }
        if (updatedShow.getTitle() == null) {
            throw new ServiceException("Show title cannot be null");
        }
        if (updatedShow.getCreation() == null) {
            throw new ServiceException("Show creation date cannot be null");
        }
        Optional<Show> opt = showRepo.findById(updatedShow.getId());
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setTitle(updatedShow.getTitle());
            s.setCreation(updatedShow.getCreation());
            s.setStars(updatedShow.getStars());
            s.setCast(updatedShow.getCast());
            
            photoServ.checkPhoto(updatedShow.getImage());
            s.setImage(updatedShow.getImage());
            
            showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }

    @Transactional
    public void shutDown(int id) throws ServiceException {
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
    public void reOpen(int id) throws ServiceException {
        Optional<Show> opt = showRepo.findById(id);
        if (opt.isPresent()) {
            Show s = opt.get();
            s.setShutdown(null);
            showRepo.save(s);
        } else {
            throw new ServiceException("Show not found");
        }
    }
}
