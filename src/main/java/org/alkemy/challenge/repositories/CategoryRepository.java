package org.alkemy.challenge.repositories;

import java.util.List;
import org.alkemy.challenge.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByName(String name);

    List<Category> findByNameIgnoreCase(String name);

    List<Category> findByNameContaining(String name);

    List<Category> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT DISTINCT c FROM Category c WHERE (SELECT p FROM Production p WHERE p.id = :id) MEMBER OF productions")
    List<Category> findByProductionId(@Param("id") int id);

    @Query("SELECT DISTINCT c FROM Category c WHERE (SELECT p FROM Production p WHERE lower(p.title) = lower(:name)) MEMBER OF productions")
    List<Category> findByProductionNameIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT c FROM Category c WHERE (SELECT p FROM Production p WHERE lower(p.title) like lower(concat('%', :name, '%'))) MEMBER OF productions")
    List<Category> findByProductionNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT c FROM Category c WHERE (SELECT f FROM Film f WHERE lower(f.title) = lower(:name)) MEMBER OF productions")
    List<Category> findByFilmNameIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT c FROM Category c WHERE (SELECT f FROM Film f WHERE lower(f.title) like lower(concat('%', :name, '%'))) MEMBER OF productions")
    List<Category> findByFilmNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT c FROM Category c WHERE (SELECT s FROM Show s WHERE lower(s.title) = lower(:name)) MEMBER OF productions")
    List<Category> findByShowNameIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT c FROM Category c WHERE (SELECT s FROM Show s WHERE lower(s.title) like lower(concat('%', :name, '%'))) MEMBER OF productions")
    List<Category> findByShowNameContainingIgnoreCase(@Param("name") String name);

    List<Category> findByShutdownIsNull();

    List<Category> findByShutdownIsNotNull();
}
