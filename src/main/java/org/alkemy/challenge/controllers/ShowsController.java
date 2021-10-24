package org.alkemy.challenge.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.controllers.pojo.DetailedProductionData;
import org.alkemy.challenge.controllers.pojo.ListProductionData;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Show;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.enumerations.Stars;
import org.alkemy.challenge.services.PhotoService;
import org.alkemy.challenge.services.ShowService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/shows")
public class ShowsController {

    @Autowired
    private ShowService showServ;

    @Autowired
    private PhotoService photoServ;

    @PostMapping(consumes = "application/json")
    public ResponseEntity add(@RequestBody Show show) {
        try {
            return new ResponseEntity(showServ.create(show), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"title"})
    public ResponseEntity add(@RequestParam String title, @RequestParam(required = false) Integer imageId, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo image = null;
            if (imageId != null) {
                image = photoServ.get(imageId);
            }
            return add(new Show(image, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"title", "file"})
    public ResponseEntity add(@RequestParam String title, @RequestParam MultipartFile file, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo image = null;
            if (file != null) {
                image = photoServ.create(file);
            }
            return add(new Show(image, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"title", "image"})
    public ResponseEntity add(@RequestParam String title, @RequestParam Photo image, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, Set<Category> categories) {
        try {
            if (image != null) {
                image = photoServ.create(image);
            }
            return add(new Show(image, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity forceAdd(@RequestBody Show show) {
        try {
            return new ResponseEntity(showServ.forceCreate(show), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping()
    public ResponseEntity getAll(@RequestParam(required = false) String title, @RequestParam(required = false) Integer genreId, @RequestParam(required = false) String order) {
        try {
            List<ListProductionData> response = new ArrayList();
            
            List<Show> titledShow = null;
            if (title != null) {
                titledShow = showServ.getByTitleLike(title);
            }

            List<Show> genreShow = null;
            if (genreId != null) {
                genreShow = showServ.getByCategory(genreId);
            }

            List<Show> shows = showServ.getAll();
            for (Show f : shows) {
                if ((titledShow == null || titledShow.contains(f)) && (genreShow == null || genreShow.contains(f))) {
                    response.add(new ListProductionData(f));
                }
            }
            
            if (order != null) {
                if (order.equalsIgnoreCase("asc")) {
                    response.sort((f1, f2) -> f1.getTitle().compareTo(f2.getTitle()));
                } else if (order.equalsIgnoreCase("desc")) {
                    System.out.println("DESCENDING");
                    response.sort((f1, f2) -> f2.getTitle().compareTo(f1.getTitle()));
                }
            }
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getDetails(@PathVariable Integer id) {
        try {
            if (id == null) {
                throw new ServiceException("ID cannot be null");
            }
            try {
                Show show = showServ.get(id);
                if (show.isDown()) {
                    return new ResponseEntity("Show is down", HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity(new DetailedProductionData(show), HttpStatus.OK);
            } catch (ServiceException ex) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", params = {"image"})
    public ResponseEntity update(@PathVariable Integer id, @RequestParam Photo image, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            if (id == null) {
                throw new ServiceException("ID cannot be null");
            }
            Show f = showServ.get(id);
            if (image != null) {
                f.setImage(photoServ.createIfNotExists(image));
            }
            if (title != null && !title.isEmpty()) {
                f.setTitle(title);
            }
            if (creation != null) {
                f.setCreation(creation);
            }
            if (stars != null) {
                f.setStars(stars);
            }
            if (cast != null) {
                f.setCast(cast);
            }
            if (categories != null) {
                f.setCategories(categories);
            }
            showServ.update(f);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestParam(required = false) Integer photoId, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo image = null;
            if (photoId != null) {
                image = photoServ.get(photoId);
            }
            return update(id, image, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", params = {"file"})
    public ResponseEntity update(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo image = null;
            if (file != null) {
                image = new Photo(file);
            }
            return update(id, image, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody Show show) {
        try {
            if (show == null) {
                throw new ServiceException("Show cannot be null");
            }
            return update(id, show.getImage(), show.getTitle(), show.getCreation(), show.getStars(), show.getCast(), show.getCategories());
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", params = {"image"})
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam Photo image, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        Show show = new Show(image, title, creation, stars, cast, categories);
        try {
            showServ.update(id, show);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            if (image != null) {
                try {
                    image = photoServ.create(image);
                } catch (ServiceException e) {
                    return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            show.setImage(image);
            show.setId(id);
            return forceAdd(show);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam Integer photoId, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo image = null;
            if (photoId != null) {
                image = photoServ.get(photoId);
            }
            return forceUpdate(id, image, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", params = {"file"})
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo image = null;
            if (file != null) {
                image = new Photo(file);
            }
            return forceUpdate(id, image, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestBody Show show) {
        try {
            if (show == null) {
                throw new ServiceException("Show cannot be null");
            }
            return forceUpdate(id, show.getImage(), show.getTitle(), show.getCreation(), show.getStars(), show.getCast(), show.getCategories());
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}/state")
    public ResponseEntity changeState(@PathVariable("id") int id) {
        try {
            Show f = showServ.get(id);
            if (f.isDown()) {
                showServ.reOpen(id);
            } else {
                showServ.shutDown(id);
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        try {
            showServ.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(ShowsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(path = "/{id}/cast")
    public ResponseEntity addCharacter(@PathVariable Integer id, @RequestParam Integer characterId){
        try{
            showServ.addCharacter(id, characterId);
            return new ResponseEntity(HttpStatus.OK);
        }catch(ServiceException ex){
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @DeleteMapping(path = "/{id}/cast")
    public ResponseEntity removeCharacter(@PathVariable Integer id, @RequestParam Integer characterId){
        try{
            showServ.removeCharacter(id, characterId);
            return new ResponseEntity(HttpStatus.OK);
        }catch(ServiceException ex){
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @PostMapping(path = "/{id}/categories", params = {"categoryId"})
    public ResponseEntity addCategory(@PathVariable Integer id, @RequestParam Integer categoryId){
        try{
            showServ.addCategory(id, categoryId);
            return new ResponseEntity(HttpStatus.OK);
        }catch(ServiceException ex){
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @PostMapping(path = "/{id}/categories", params = {"category"})
    public ResponseEntity addCategory(@PathVariable Integer id, @RequestParam String category){
        try{
            showServ.addCategory(id, category);
            return new ResponseEntity(HttpStatus.OK);
        }catch(ServiceException ex){
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @DeleteMapping(path = "/{id}/categories", params = {"categoryId"})
    public ResponseEntity removeCategory(@PathVariable Integer id, @RequestParam Integer categoryId){
        try{
            showServ.removeCategory(id, categoryId);
            return new ResponseEntity(HttpStatus.OK);
        }catch(ServiceException ex){
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @DeleteMapping(path = "/{id}/categories", params = {"category"})
    public ResponseEntity removeCategory(@PathVariable Integer id, @RequestParam String category){
        try{
            showServ.removeCategory(id, category);
            return new ResponseEntity(HttpStatus.OK);
        }catch(ServiceException ex){
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
