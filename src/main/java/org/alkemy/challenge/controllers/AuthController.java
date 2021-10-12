package org.alkemy.challenge.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.services.DisneyUserService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private DisneyUserService userServ;
    
    @PostMapping("/register")
    public ResponseEntity register(@RequestParam String email, @RequestParam String password1, @RequestParam String password2){
        try {
            if(!password1.equals(password2)){
                throw new ServiceException("Passwords don't match");
            }
            return new ResponseEntity(userServ.create(email, password1), HttpStatus.CREATED);
        } catch (ServiceException ex) {
            Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(ex.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
}
