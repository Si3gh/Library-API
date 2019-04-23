package br.com.db1.demo.repository;

import br.com.db1.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Long> {

//
//    @Query(value = "select * from person_aud where id = :id", nativeQuery = true)
//    List<Object> findAllById(@Param("id") Long id);
}

