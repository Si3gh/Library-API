package br.com.demo.audit.repository;

import br.com.demo.audit.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Long> {

//
//    @Query(value = "select * from person_aud where id = :id", nativeQuery = true)
//    List<Object> findAllById(@Param("id") Long id);
}

