package org.alkemy.challenge.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.controllers.pojo.DetailedProductionData;
import org.alkemy.challenge.controllers.pojo.ListProductionData;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.services.ProductionService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productions")
public class ProductionsController {
    
    @Autowired
    private ProductionService prodServ;
    
    @GetMapping()
    public ResponseEntity getAll(@RequestParam(required = false) String title, @RequestParam(required = false) Integer genreId, @RequestParam(required = false) String order) {
        try {
            List<ListProductionData> response = new ArrayList();
            
            List<Production> titledProduction = null;
            if (title != null) {
                titledProduction = prodServ.getByTitleLike(title);
            }

            List<Production> genreProduction = null;
            if (genreId != null) {
                genreProduction = prodServ.getByCategory(genreId);
            }

            List<Production> prods = prodServ.getAll();
            for (Production f : prods) {
                if ((titledProduction == null || titledProduction.contains(f)) && (genreProduction == null || genreProduction.contains(f))) {
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
    public ResponseEntity get(@PathVariable Integer id){
        try {
            if(id == null){
                throw new ServiceException("ID cannot be null");
            }
            try{
                Production p = prodServ.get(id);
                return new ResponseEntity(new DetailedProductionData(p), HttpStatus.CREATED);
            } catch (ServiceException ex){
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (ServiceException ex) {
            Logger.getLogger(ProductionsController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
}
