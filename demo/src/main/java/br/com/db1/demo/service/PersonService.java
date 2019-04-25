package br.com.db1.demo.service;

import br.com.db1.demo.model.Person;
import br.com.db1.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Person persistPerson(Person person) {
        return personRepository.save(person);
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
    }
}


