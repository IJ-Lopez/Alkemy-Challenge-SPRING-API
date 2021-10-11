package org.alkemy.challenge.repositories;

import java.util.List;
import org.alkemy.challenge.entities.DisneyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisneyUserRepository extends JpaRepository<DisneyUser, Integer>{
    
    List<DisneyUser> findByEmailContainingIgnoreCase(String email);
    
    List<DisneyUser> findByEmailIgnoreCase(String email);
    
}
