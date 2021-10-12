package org.alkemy.challenge.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        AnimatedCharacter character;
        List<ListCharacterData> response = new ArrayList<>();
        for (int i = 0; i < characters.size(); i++) {
            character = characters.get(i);
            ListCharacterData data = new ListCharacterData();
            data.setImage(photoServ.getResponseEntity(character.getImage()));
            data.setId(character.getId());
            data.setName(character.getName());
            response.add(data);
        }
        return response;
    }

//    @PostMapping(value = "/add", params = {"character"})
//    public ResponseEntity create(@RequestBody AnimatedCharacter character) {
//        try {
//            return new ResponseEntity(acServ.create(character), HttpStatus.CREATED);
//        } catch (ServiceException ex) {
//            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
//            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//    }

    @PostMapping(value = "/add", params={"name"})
    public ResponseEntity create(@RequestParam String name, @RequestParam(required = false) Integer photoId, @RequestParam(required = false) Integer age, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String lore, @RequestParam(required = false) List<Production> productions) {
        try {
            Photo photo = null;
            if (photoId != null) {
                photo = photoServ.get(photoId);
            }
            AnimatedCharacter character = new AnimatedCharacter(photo, name, age, weight, lore, productions);
            return new ResponseEntity(acServ.create(photo, name, 0, 0, lore, productions), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(path = "/{id}")
    public Object[] getDetails(ModelMap model, @PathVariable("id") Integer id) {
        try {
            if (id == null) {
                throw new ServiceException();
            }
            AnimatedCharacter ac = acServ.get(id);
            Object[] details = new Object[2];
            details[0] = photoServ.getResponseEntity(ac.getImage());

            String description = String.format("ID : %d || NAME : %s%n"
                    + "Age: %d || Weight: %d%n"
                    + "Lore: %s%n"
                    + "Appears in:%n",
                    ac.getId(), ac.getName(), ac.getAge(), ac.getWeight(), ac.getLore());
            for (Production p : ac.getAssociateProductions()) {
                description += String.format("* %s%n", p.getTitle());
            }
            details[1] = description;
            return details;
        } catch (ServiceException ex) {
            model.put("error", ex.getMessage());
            Logger.getLogger(AnimatedCharacterController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @PostMapping("/updateImage")
    public void updatePhoto(@RequestParam("id") int id, @RequestParam("photo") MultipartFile file) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            Photo photo = new Photo(file);
            ac.setImage(photo);
            acServ.update(ac);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/updateName")
    public void updateName(@RequestParam("id") int id, @RequestParam("name") String name) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            ac.setName(name);
            acServ.update(ac);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/updateLore")
    public void updateLore(@RequestParam("id") int id, @RequestParam("lore") String lore) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            ac.setLore(lore);
            acServ.update(ac);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/updateWeight")
    public void updateWeight(@RequestParam("id") int id, @RequestParam("weight") int weight) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            ac.setWeight(weight);
            acServ.update(ac);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/updateAge")
    public void updateAge(@RequestParam("id") int id, @RequestParam("age") int age) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            ac.setWeight(age);
            acServ.update(ac);
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger
                    .getLogger(AnimatedCharacterController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostMapping("/addProduction") //NO SE ESTÁ MODIFICACNDO LA BASE DE DATOS
    public void addProduction(@RequestParam("id") int id, @RequestParam("productionId") int productionId) {
        try {
            AnimatedCharacter ac = acServ.get(id);
            Production production = prodServ.getById(id);
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
            ac.getAssociateProductions().remove(prodServ.getById(id));
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
