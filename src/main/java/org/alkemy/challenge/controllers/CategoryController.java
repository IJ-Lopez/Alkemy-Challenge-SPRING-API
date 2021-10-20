package org.alkemy.challenge.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.controllers.pojo.DetailedCategoryData;
import org.alkemy.challenge.controllers.pojo.ListCategoryData;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.services.CategoryService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService catServ;
    
    @GetMapping()
    public ResponseEntity getAll(){
        List<Category> categories = catServ.getAll();
        List<ListCategoryData> response = new ArrayList();
        categories.forEach(p -> {
           response.add(new ListCategoryData(p));
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
                Category c = catServ.get(id);
                return new ResponseEntity(new DetailedCategoryData(c), HttpStatus.CREATED);
            } catch (ServiceException ex){
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (ServiceException ex) {
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
