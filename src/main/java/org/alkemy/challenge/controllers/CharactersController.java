package org.alkemy.challenge.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.controllers.pojo.DetailedCharacterData;
import org.alkemy.challenge.controllers.pojo.ListCharacterData;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.services.AnimatedCharacterService;
import org.alkemy.challenge.services.PhotoService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
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
@RequestMapping("/characters")
public class CharactersController {

    @Autowired
    private AnimatedCharacterService acServ;

    @Autowired
    private PhotoService photoServ;

    @GetMapping()
    public ResponseEntity getAll(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer productionId) {
        try {
            List<ListCharacterData> response = new ArrayList<>();
            List<AnimatedCharacter> characters = acServ.getAll();
            
            List<AnimatedCharacter> nameCharacters = null;
            if (name != null) {
                nameCharacters = acServ.getByNameLike(name);
            }
            
            List<AnimatedCharacter> ageCharacters = null;
            if (age != null) {
                ageCharacters = acServ.getByAge(age);
            }
            
            List<AnimatedCharacter> productionCharacters = null;
            if (productionId != null){
                productionCharacters = acServ.getByProduction(productionId);
            }
            
            for (AnimatedCharacter c : characters) {
                if ((nameCharacters == null || nameCharacters.contains(c)) && (ageCharacters == null || ageCharacters.contains(c)) && (productionCharacters == null || productionCharacters.contains(c))) {
                    response.add(new ListCharacterData(c));
                }
            }
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(params = {"character"})
    public ResponseEntity add(@RequestBody AnimatedCharacter character) {
        try {
            return new ResponseEntity(acServ.create(character), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"name"})
    public ResponseEntity add(@RequestParam String name, @RequestParam(required = false) Integer imageId, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (imageId != null) {
                image = photoServ.get(imageId);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore);
            return add(character);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"name", "file"})
    public ResponseEntity add(@RequestParam String name, @RequestParam MultipartFile file, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (file != null) {
                image = photoServ.create(file);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore);
            return add(character);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(params = {"name", "image"})
    public ResponseEntity add(@RequestParam String name, @RequestParam Photo image, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            if (image != null) {
                image = photoServ.create(image);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore);
            return add(character);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(params = {"character"})
    public ResponseEntity forceAdd(@RequestBody AnimatedCharacter character) {
        try {
            return new ResponseEntity(acServ.forceCreate(character), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(params = {"name"})
    public ResponseEntity forceAdd(@RequestParam String name, @RequestParam(required = false) Integer imageId, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (imageId != null) {
                image = photoServ.get(imageId);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore);
            return forceAdd(character);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(params = {"name", "file"})
    public ResponseEntity forceAdd(@RequestParam String name, @RequestParam MultipartFile file, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (file != null) {
                image = photoServ.create(file);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore);
            return forceAdd(character);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(params = {"name", "image"})
    public ResponseEntity forceAdd(@RequestParam String name, @RequestParam Photo image, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            if (image != null) {
                image = photoServ.create(image);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore);
            return forceAdd(character);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getDetails(ModelMap model, @PathVariable("id") Integer id) {
        try {
            if (id == null) {
                throw new ServiceException("ID cannot be null");
            }
            try {
                AnimatedCharacter ac = acServ.get(id);
                if (ac.isDown()) {
                    return new ResponseEntity("Animated Character is down", HttpStatus.FORBIDDEN);
                }
                DetailedCharacterData lcd = new DetailedCharacterData(ac);
                return new ResponseEntity(lcd, HttpStatus.OK);
            } catch (ServiceException ex) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", params = {"image"})
    public ResponseEntity update(@PathVariable Integer id, @RequestParam Photo image, @RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            if (id == null) {
                throw new ServiceException("ID cannot be null");
            }
            AnimatedCharacter ac = acServ.get(id);
            if (image != null) {
                ac.setImage(photoServ.createIfNotExists(image));
            }
            if (name != null && !name.isEmpty()) {
                ac.setName(name);
            }
            if (age != null) {
                ac.setAge(age);
            }
            if (weight != null && weight > 0) {
                ac.setWeight(weight);
            }
            if (lore != null) {
                ac.setLore(lore);
            }
            acServ.update(ac);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestParam(required = false) Integer photoId, @RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (photoId != null) {
                image = photoServ.get(photoId);
            }
            return update(id, image, name, age, weight, lore);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", params = {"file"})
    public ResponseEntity update(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (file != null) {
                image = new Photo(file);
            }
            return update(id, image, name, age, weight, lore);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PatchMapping(path = "/{id}", params = {"character"})
    public ResponseEntity update(@PathVariable Integer id, @RequestBody AnimatedCharacter ac) {
        return update(id, ac.getImage(), ac.getName(), ac.getAge(), ac.getWeight(), ac.getLore());

    }

    @PutMapping(path = "/{id}", params = {"image"})
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam Photo image, @RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        AnimatedCharacter ac = new AnimatedCharacter(image, name, age, weight, lore);
        try {
            acServ.update(id, ac);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException e) {
            if (image != null) {
                try {
                    image = photoServ.create(image);
                } catch (ServiceException ex) {
                    return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            ac.setImage(image);
            ac.setId(id);
            return forceAdd(ac);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam(required = false) Integer photoId, @RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (photoId != null) {
                image = photoServ.get(photoId);
            }
            return forceUpdate(id, image, name, age, weight, lore);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", params = {"file"})
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore) {
        try {
            Photo image = null;
            if (file != null) {
                image = new Photo(file);
            }
            return forceUpdate(id, image, name, age, weight, lore);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(path = "/{id}", params = {"character"})
    public ResponseEntity forceUpdate(@PathVariable Integer id, @RequestBody AnimatedCharacter ac) {
        return forceUpdate(id, ac.getImage(), ac.getName(), ac.getAge(), ac.getWeight(), ac.getLore());
    }

    @PatchMapping(path = "/{id}/state")
    public ResponseEntity changeState(@PathVariable("id") int id) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            if (ac.isDown()) {
                acServ.reOpen(id);
            } else {
                acServ.shutDown(id);
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        try {
            acServ.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(CharactersController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
