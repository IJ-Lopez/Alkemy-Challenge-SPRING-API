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
import org.alkemy.challenge.entities.Film;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.enumerations.Stars;
import org.alkemy.challenge.services.FilmService;
import org.alkemy.challenge.services.PhotoService;
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
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private FilmService filmServ;

    @Autowired
    private PhotoService photoServ;

    @PostMapping(consumes = "application/json")
    public ResponseEntity add(@RequestBody Film movie) {
        try {
            return new ResponseEntity(filmServ.create(movie), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"title"})
    public ResponseEntity add(@RequestParam String title, @RequestParam(required = false) Integer photoId, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo photo = null;
            if (photoId != null) {
                photo = photoServ.get(photoId);
            }
            return add(new Film(photo, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"title", "file"})
    public ResponseEntity add(@RequestParam String title, @RequestParam MultipartFile file, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo photo = null;
            if (file != null) {
                photo = photoServ.create(file);
            }
            return add(new Film(photo, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"title", "photo"})
    public ResponseEntity add(@RequestParam String title, @RequestParam Photo photo, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, Set<Category> categories) {
        try {
            if (photo != null) {
                photo = photoServ.create(photo);
            }
            return add(new Film(photo, title, creation, stars, cast, categories));
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity forceAdd(@RequestBody Film movie) {
        try {
            return new ResponseEntity(filmServ.forceCreate(movie), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping()
    public ResponseEntity getAll(@RequestParam(required = false) String title, @RequestParam(required = false) Integer genreId, @RequestParam(required = false) String order) {
        try {
            List<ListProductionData> response = new ArrayList();
            
            List<Film> titledFilm = null;
            if (title != null) {
                titledFilm = filmServ.getByNameLike(title);
            }

            List<Film> genreFilm = null;
            if (genreId != null) {
                genreFilm = filmServ.getByCategory(genreId);
            }

            List<Film> films = filmServ.getAll();
            for (Film f : films) {
                if ((titledFilm == null || titledFilm.contains(f)) && (genreFilm == null || genreFilm.contains(f))) {
                    response.add(new ListProductionData(f));
                }
            }
            System.out.println(order);
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
                Film film = filmServ.get(id);
                if (film.isDown()) {
                    return new ResponseEntity("Film is down", HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity(new DetailedProductionData(film), HttpStatus.OK);
            } catch (ServiceException ex) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", params = {"photo"})
    public ResponseEntity update(@PathVariable Integer id, @RequestParam Photo photo, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            if (id == null) {
                throw new ServiceException("ID cannot be null");
            }
            Film f = filmServ.get(id);
            if (photo != null) {
                f.setImage(photoServ.createIfNotExists(photo));
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
            filmServ.update(f);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestParam(required = false) Integer photoId, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo photo = null;
            if (photoId != null) {
                photo = photoServ.get(photoId);
            }
            return update(id, photo, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", params = {"file"})
    public ResponseEntity update(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo photo = null;
            if (file != null) {
                photo = new Photo(file);
            }
            return update(id, photo, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody Film movie) {
        try {
            if (movie == null) {
                throw new ServiceException("Film cannot be null");
            }
            return update(id, movie.getImage(), movie.getTitle(), movie.getCreation(), movie.getStars(), movie.getCast(), movie.getCategories());
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", params = {"photo"})
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam Photo photo, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        Film film = new Film(photo, title, creation, stars, cast, categories);
        try {
            filmServ.update(id, film);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            if (photo != null) {
                try {
                    photo = photoServ.create(photo);
                } catch (ServiceException e) {
                    return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            film.setImage(photo);
            film.setId(id);
            return forceAdd(film);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam Integer photoId, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo photo = null;
            if (photoId != null) {
                photo = photoServ.get(photoId);
            }
            return forceUpdate(id, photo, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", params = {"file"})
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam(required = false) String title, @RequestParam(required = false) Date creation, @RequestParam(required = false) Stars stars, @RequestParam(required = false) Set<AnimatedCharacter> cast, @RequestParam(required = false) Set<Category> categories) {
        try {
            Photo photo = null;
            if (file != null) {
                photo = new Photo(file);
            }
            return forceUpdate(id, photo, title, creation, stars, cast, categories);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestBody Film movie) {
        try {
            if (movie == null) {
                throw new ServiceException("Film cannot be null");
            }
            return forceUpdate(id, movie.getImage(), movie.getTitle(), movie.getCreation(), movie.getStars(), movie.getCast(), movie.getCategories());
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}/state")
    public ResponseEntity changeState(@PathVariable("id") int id) {
        try {
            Film f = filmServ.get(id);
            if (f.isDown()) {
                filmServ.reOpen(id);
            } else {
                filmServ.shutDown(id);
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        try {
            filmServ.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/{id}/cast")
    public ResponseEntity addCharacter(@PathVariable Integer id, @RequestParam Integer characterId) {
        try {
            filmServ.addCharacter(id, characterId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping(path = "/{id}/cast")
    public ResponseEntity removeCharacter(@PathVariable Integer id, @RequestParam Integer characterId) {
        try {
            filmServ.removeCharacter(id, characterId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(path = "/{id}/categories", params = {"categoryId"})
    public ResponseEntity addCategory(@PathVariable Integer id, @RequestParam Integer categoryId) {
        try {
            filmServ.addCategory(id, categoryId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(path = "/{id}/categories", params = {"category"})
    public ResponseEntity addCategory(@PathVariable Integer id, @RequestParam String category) {
        try {
            filmServ.addCategory(id, category);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping(path = "/{id}/categories", params = {"categoryId"})
    public ResponseEntity removeCategory(@PathVariable Integer id, @RequestParam Integer categoryId) {
        try {
            filmServ.removeCategory(id, categoryId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping(path = "/{id}/categories", params = {"category"})
    public ResponseEntity removeCategory(@PathVariable Integer id, @RequestParam String category) {
        try {
            filmServ.removeCategory(id, category);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(MoviesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
