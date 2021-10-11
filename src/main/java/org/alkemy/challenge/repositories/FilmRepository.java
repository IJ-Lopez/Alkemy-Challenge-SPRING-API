package org.alkemy.challenge.repositories;

import java.util.List;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface FilmRepository extends ProductionBaseRepository<Film>{
    
    @Query("SELECT DISTINCT f FROM Film f WHERE (SELECT ac FROM AnimatedCharacter ac WHERE ac.id = :id) MEMBER OF cast")
    List<Film> findByCharacterId(@Param("id") int id);
    
    @Query("SELECT DISTINCT f FROM Film f WHERE (SELECT ac FROM AnimatedCharacter ac WHERE lower(ac.name) = lower(:name)) MEMBER OF cast")
    List<Film> findByCharacterNameIgnoreCase(@Param("name") String name);
    
    @Query("SELECT DISTINCT f FROM Film f WHERE (SELECT ac FROM AnimatedCharacter ac WHERE lower(ac.name) LIKE lower(concat('%', :name, '%'))) MEMBER OF cast")
    List<Film> findByCharacterNameContainingIgnoreCase(@Param("name") String name);
    
}