package org.alkemy.challenge.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.controllers.pojo.DetailedCharacterData;
import org.alkemy.challenge.controllers.pojo.ListCharacterData;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.services.AnimatedCharacterService;
import org.alkemy.challenge.services.PhotoService;
import org.alkemy.challenge.services.ProductionService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/characters")
public class AnimatedCharacterController {

    @Autowired
    private AnimatedCharacterService acServ;

    @Autowired
    private PhotoService photoServ;

    @Autowired
    private ProductionService prodServ;

    @GetMapping()
    public List<ListCharacterData> getAll() {
        List<AnimatedCharacter> characters = acServ.getAll();
        List<ListCharacterData> response = new ArrayList<>();
        characters.forEach(c -> {
           response.add(new ListCharacterData(c));
        });
        return response;
    }

    @PostMapping(value = "/add", params = {"character"})
    public ResponseEntity add(@RequestBody AnimatedCharacter character) {
        try {
            return new ResponseEntity(acServ.create(character), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/add", params = {"name"})
    public ResponseEntity add(@RequestParam String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore, @RequestParam(required = false) Set<Production> productions) {
        Photo photo = null;
        AnimatedCharacter character = new AnimatedCharacter(photo, name, age, weight, lore, productions);
        return add(character);
    }

    @PostMapping(value = "/add", params = {"name", "imageId"})
    public ResponseEntity add(@RequestParam String name, @RequestParam Integer imageId, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore, @RequestParam(required = false) Set<Production> productions) {
        try {
            Photo image = null;
            if (imageId != null) {
                image = photoServ.get(imageId);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore, productions);
            return add(character);
        } catch (ServiceException ex) {
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/add", params = {"name", "file"})
    public ResponseEntity create(@RequestParam String name, @RequestParam MultipartFile file, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore, @RequestParam(required = false) Set<Production> productions) {
        try {
            Photo image = null;
            if (file != null) {
                image = photoServ.create(file);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore, productions);
            return add(character);
        } catch (ServiceException ex) {
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/add", params = {"name", "image"})
    public ResponseEntity create(@RequestParam String name, @RequestParam Photo image, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore, @RequestParam(required = false) Set<Production> productions) {
        try {
            if (image != null) {
                image = photoServ.create(image);
            }
            AnimatedCharacter character = new AnimatedCharacter(image, name, age, weight, lore, productions);
            return add(character);
        } catch (ServiceException ex) {
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
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
                DetailedCharacterData lcd = new DetailedCharacterData(ac);
                return new ResponseEntity(lcd, HttpStatus.OK);
            } catch (ServiceException ex) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (ServiceException ex) {
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "/update", params = {"id"})
    public ResponseEntity updatePhoto(@RequestParam Integer id, @RequestParam(required = false) Integer photoId, @RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore, @RequestParam(required = false) Set<Production> productions) {
        try {
            if(id == null){
                throw new ServiceException("ID cannot be null");
            }
            AnimatedCharacter ac = acServ.get(id);
            if(photoId != null){
                ac.setImage(photoServ.get(photoId));    
            }
            if(name != null && !name.isEmpty()){
                ac.setName(name);
            }
            if(age != null){
                ac.setAge(age);
            }
            if(weight != null && weight > 0){
                ac.setWeight(weight);
            }
            if(lore != null){
                ac.setLore(lore);
            }
            if(productions != null){
                ac.setAssociateProductions(productions);
            }
            acServ.update(ac);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException ex) {
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/addProduction") //NO SE ESTÁ MODIFICACNDO LA BASE DE DATOS
    public void addProduction(@RequestParam("id") int id, @RequestParam("productionId") int productionId) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            Production production = prodServ.get(id);
            if (ac.getAssociateProductions().contains(production)) {
                throw new ServiceException("Character was already in this production");
            }
            ac.getAssociateProductions().add(production);
            acServ.update(ac);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/removeProduction") //NO SE ESTÁ MODIFICACNDO LA BASE DE DATOS
    public void removeProduction(@RequestParam("id") int id, @RequestParam("productionId") int productionId) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            ac.getAssociateProductions().remove(prodServ.get(id));
            acServ.update(ac);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/shutdown")
    public void shutDown(@RequestParam("id") int id) {
        try {
            acServ.shutDown(id);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/reOpen")
    public void reOpen(@RequestParam("id") int id) {
        try {
            acServ.reOpen(id);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
