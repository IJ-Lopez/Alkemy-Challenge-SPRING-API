package org.alkemy.challenge.controllers;

import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Film;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.enumerations.Stars;
import org.alkemy.challenge.services.FilmService;
import org.alkemy.challenge.services.PhotoService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movies")
public class FilmController {

    @Autowired
    private FilmService filmServ;

    @Autowired
    private PhotoService photoServ;

    @PostMapping(value = "/add", params = {"movie"})
    public ResponseEntity add(@RequestBody Film movie) {
        try {
            return new ResponseEntity(filmServ.create(movie), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/add", params = {"title"})
    public ResponseEntity add(@RequestParam String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, Set<Category> categories) {
        return add(new Film(null, title, creation, stars, cast, categories));
    }

    @PostMapping(value = "/add", params = {"title", "imageId"})
    public ResponseEntity add(@RequestParam String title, @RequestParam Integer imageId, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, Set<Category> categories) {
        try {
            Photo image = null;
            if (imageId != null) {
                image = photoServ.get(imageId);
            }
            return add(new Film(image, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/add", params = {"title", "file"})
    public ResponseEntity add(@RequestParam String title, @RequestParam MultipartFile file, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, Set<Category> categories) {
        try {
            Photo image = null;
            if (file != null) {
                image = photoServ.create(file);
            }
            return add(new Film(image, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/add", params = {"title", "image"})
    public ResponseEntity add(@RequestParam String title, @RequestParam Photo image, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, Set<Category> categories) {
        try {
            if (image != null) {
                image = photoServ.create(image);
            }
            return add(new Film(image, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    
}
