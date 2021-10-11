package org.alkemy.challenge.repositories;

import java.util.List;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.Show;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface ShowRepository extends ProductionBaseRepository<Show>{
    
    @Query("SELECT DISTINCT s FROM Show s WHERE (SELECT ac FROM AnimatedCharacter ac WHERE ac.id = :id) MEMBER OF cast")
    List<Show> findByCharacterId(@Param("id") int id);
    
    @Query("SELECT DISTINCT s FROM Show s WHERE (SELECT ac FROM AnimatedCharacter ac WHERE lower(ac.name) = lower(:name)) MEMBER OF cast")
    List<Show> findByCharacterNameIgnoreCase(@Param("name") String name);
    
    @Query("SELECT DISTINCT s FROM Show s WHERE (SELECT ac FROM AnimatedCharacter ac WHERE lower(ac.name) LIKE lower(concat('%', :name, '%'))) MEMBER OF cast")
    List<Show> findByCharacterNameContainingIgnoreCase(@Param("name") String name);
    
}
