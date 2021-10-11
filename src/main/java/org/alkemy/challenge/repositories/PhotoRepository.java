package org.alkemy.challenge.repositories;

import java.util.List;
import org.alkemy.challenge.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer>{
    
    List<Photo> findByNameIgnoreCase(String name);
    
    List<Photo> findByNameContainingIgnoreCase(String name);
}
