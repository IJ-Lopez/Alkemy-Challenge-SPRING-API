package org.alkemy.challenge.repositories;

import java.util.List;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimatedCharacterRepository extends JpaRepository<AnimatedCharacter, Integer>{
    List<AnimatedCharacter> findByName(String name);
    
    List<AnimatedCharacter> findByNameIgnoreCase(String name);
    
    List<AnimatedCharacter> findByNameContaining(String name);
    
    List<AnimatedCharacter> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT DISTINCT a FROM AnimatedCharacter a WHERE (SELECT p FROM Production p WHERE p.id = :id) MEMBER OF associateProductions")
    List<AnimatedCharacter> findByProductionId(@Param("id") int id);
    
    @Query("SELECT DISTINCT a FROM AnimatedCharacter a WHERE (SELECT p FROM Production p WHERE p.title = :name) MEMBER OF associateProductions")
    List<AnimatedCharacter> findByProductionNameIgnoreCase(@Param("name") String name);
    
    @Query("SELECT DISTINCT a FROM AnimatedCharacter a WHERE (SELECT p FROM Production p WHERE lower(p.title) like lower(concat('%', :name, '%'))) MEMBER OF associateProductions")
    List<AnimatedCharacter> findByProductionNameContainingIgnoreCase(@Param("name") String name);
    
    @Query("SELECT DISTINCT a FROM AnimatedCharacter a WHERE (SELECT f FROM Film f WHERE lower(f.title) = lower(:name)) MEMBER OF associateProductions")
    List<AnimatedCharacter> findByFilmNameIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT a FROM AnimatedCharacter a WHERE (SELECT f FROM Film f WHERE lower(f.title) like lower(concat('%', :name, '%'))) MEMBER OF associateProductions")
    List<AnimatedCharacter> findByFilmNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT a FROM AnimatedCharacter a WHERE (SELECT s FROM Show s WHERE lower(s.title) = lower(:name)) MEMBER OF associateProductions")
    List<AnimatedCharacter> findByShowNameIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT a FROM AnimatedCharacter a WHERE (SELECT s FROM Show s WHERE lower(s.title) like lower(concat('%', :name, '%'))) MEMBER OF associateProductions")
    List<AnimatedCharacter> findByShowNameContainingIgnoreCase(@Param("name") String name);
    
    List<AnimatedCharacter> findByShutdownIsNull();
    
    List<AnimatedCharacter> findByShutdownIsNotNull();
}
