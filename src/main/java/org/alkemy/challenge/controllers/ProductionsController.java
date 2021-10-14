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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productions")
public class ProductionsController {
    
    @Autowired
    private ProductionService prodServ;
    
    @GetMapping()
    public ResponseEntity getAll(){
        List<Production> productions = prodServ.getAll();
        List<ListProductionData> response = new ArrayList();
        productions.forEach(p -> {
           response.add(new ListProductionData(p));
        });
        return new ResponseEntity(response, HttpStatus.OK);
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
