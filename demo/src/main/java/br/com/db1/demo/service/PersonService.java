package br.com.db1.demo.service;

import br.com.db1.demo.model.Person;

import br.com.db1.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Service
public class PersonService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    public Optional<Person> getPerson(Long id) {
        return personRepository.findById(id);
    }


}
