package org.alkemy.challenge.repositories;

import java.util.List;
import org.alkemy.challenge.entities.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface ProductionBaseRepository<T extends Production> extends JpaRepository<T, Integer>{
    
    List<T> findByTitle(String title);
    
    List<T> findByTitleIgnoreCase(String title);
    
    List<T> findByTitleContaining(String title);
    
    List<T> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT DISTINCT p FROM Production p WHERE (SELECT c FROM Category c WHERE c.id = :id) MEMBER OF p.categories")
    List<T> findByCategoryId(@Param("id") int id);
    
    @Query("SELECT DISTINCT p FROM Production p WHERE (SELECT c FROM Category c WHERE lower(c.name) = lower(:name)) MEMBER OF p.categories")
    List<T> findByCategoryNameIgnoreCase(@Param("name") String name);
    
    @Query("SELECT DISTINCT p FROM Production p WHERE (SELECT c FROM Category c WHERE lower(c.name) LIKE lower(concat('%', :name, '%'))) MEMBER OF p.categories")
    List<T> findByCategoryNameContainingIgnoreCase(@Param("name") String name);
    
    List<T> findAllByOrderByCreationAsc();
    
    List<T> findAllByOrderByCreationDesc();
    
    List<T> findByShutdownIsNull();
    
    List<T> findByShutdownIsNotNull();
    
}
