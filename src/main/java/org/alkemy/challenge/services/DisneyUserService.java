package org.alkemy.challenge.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.DisneyUser;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.alkemy.challenge.repositories.DisneyUserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.userdetails.User;

@Service
public class DisneyUserService implements UserDetailsService {

    @Autowired
    private DisneyUserRepository userRepo;

    @Transactional
    public void create(DisneyUser user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User cannot be null");
        }
        if (user.getId() != null && userRepo.findById(user.getId()).isPresent()) {
            throw new ServiceException("User ID already exists");
        }
        if (user.getEmail() == null) {
            throw new ServiceException("User email cannot be null");
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ServiceException("User email not valid");
        }
        if (user.getPassword() == null) {
            throw new ServiceException("User password cannot be null");
        }
        checkEmail(user.getEmail());
        userRepo.save(user);
    }

    @Transactional
    public DisneyUser create(String email, String password) throws ServiceException {
        if (email == null) {
            throw new ServiceException("User email cannot be null");
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ServiceException("User email not valid");
        }
        if (password == null) {
            throw new ServiceException("User password cannot be null");
        }
        checkEmail(email);
        return userRepo.save(new DisneyUser(email, password));
    }

    public List<DisneyUser> getAll() {
        return userRepo.findAll();
    }

    public DisneyUser get(String id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("User ID cannot be null");
        }
        Optional<DisneyUser> opt = userRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("User not found");
        }
    }

    public DisneyUser getByEmail(String mail) throws ServiceException {
        if (mail == null) {
            throw new ServiceException("User mail cannot be null");
        }
        List<DisneyUser> users = userRepo.findByEmailIgnoreCase(mail);
        if (users.isEmpty()) {
            throw new ServiceException("User not found");
        }
        return users.get(0);
    }

    public List<DisneyUser> getByEmailLike(String mail) throws ServiceException {
        if (mail == null) {
            throw new ServiceException("User mail cannot be null");
        }
        return userRepo.findByEmailContainingIgnoreCase(mail);
    }

    @Transactional
    public void update(String id, String mail, String password) throws ServiceException {
        if (id == null) {
            throw new ServiceException("User ID cannot be null");
        }
        if (mail == null) {
            throw new ServiceException("User mail cannot be null");
        }
        if (!EmailValidator.getInstance().isValid(mail)) {
            throw new ServiceException("User email not valid");
        }
        if (password == null) {
            throw new ServiceException("User password cannot be null");
        }
        Optional<DisneyUser> opt = userRepo.findById(id);
        if (opt.isPresent()) {
            DisneyUser user = opt.get();

            checkEmail(mail);
            user.setEmail(mail);
            user.setPassword(password);
            userRepo.save(user);
        } else {
            throw new ServiceException("User not found");
        }
    }

    @Transactional
    public void update(String id, DisneyUser updatedUser) throws ServiceException {
        if(id == null){
            throw new ServiceException("User ID cannot be null");
        }
        if (updatedUser == null) {
            throw new ServiceException("User cannot be null");
        }
        if (updatedUser.getEmail() == null) {
            throw new ServiceException("User mail cannot be null");
        }
        if (!EmailValidator.getInstance().isValid(updatedUser.getEmail())) {
            throw new ServiceException("User email not valid");
        }
        if (updatedUser.getPassword() == null) {
            throw new ServiceException("User password cannot be null");
        }
        Optional<DisneyUser> opt = userRepo.findById(id);
        if (opt.isPresent()) {
            DisneyUser user = opt.get();

            checkEmail(updatedUser.getEmail());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            userRepo.save(user);
        } else {
            throw new ServiceException("User not found");
        }
    }

    @Transactional
    public void update(DisneyUser updatedUser) throws ServiceException {
        if (updatedUser == null) {
            throw new ServiceException("User cannot be null");
        }
        if (updatedUser.getId() == null) {
            throw new ServiceException("User ID cannot be null");
        }
        if (updatedUser.getEmail() == null) {
            throw new ServiceException("User mail cannot be null");
        }
        if (!EmailValidator.getInstance().isValid(updatedUser.getEmail())) {
            throw new ServiceException("User email not valid");
        }
        if (updatedUser.getPassword() == null) {
            throw new ServiceException("User password cannot be null");
        }
        Optional<DisneyUser> opt = userRepo.findById(updatedUser.getId());
        if (opt.isPresent()) {
            DisneyUser user = opt.get();

            checkEmail(updatedUser.getEmail());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            userRepo.save(user);
        } else {
            throw new ServiceException("User not found");
        }
    }

    @Transactional
    public void shutDown(String id) throws ServiceException {
        Optional<DisneyUser> opt = userRepo.findById(id);
        if (opt.isPresent()) {
            DisneyUser user = opt.get();
            user.setShutdown(new Date());
            userRepo.save(user);
        } else {
            throw new ServiceException("User not found");
        }
    }

    @Transactional
    public void reOpen(String id) throws ServiceException {
        if(id == null){
            throw new ServiceException("User ID cannot be null");
        }
        Optional<DisneyUser> opt = userRepo.findById(id);
        if (opt.isPresent()) {
            DisneyUser user = opt.get();
            user.setShutdown(null);
            userRepo.save(user);
        } else {
            throw new ServiceException("User not found");
        }
    }

    private void checkEmail(String mail) throws ServiceException {
        if (!userRepo.findByEmailIgnoreCase(mail).isEmpty()) {
            throw new ServiceException("This mail already exists");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        DisneyUser user = userRepo.findByEmailIgnoreCase(email).get(0);
        if (user != null) {
            List<GrantedAuthority> authorities = new ArrayList() {
                {
                    add(new SimpleGrantedAuthority("CHARACTERS_MODULE"));
                    add(new SimpleGrantedAuthority("MOVIES_MODULE"));
                }
            };
            User springUser = new User(user.getEmail(), user.getPassword(), authorities);
            return springUser;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
