package org.alkemy.challenge.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.alkemy.challenge.services.DisneyUserService;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String register(ModelMap model, @RequestParam String email, @RequestParam String password){
        try {
            userServ.create(email, password);
        } catch (ServiceException ex) {
            model.put("error", ex.getMessage());
            Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
            return "register.html";
        }
        return "home.html";
    }
    
}
