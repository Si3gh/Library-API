package br.com.db1.demo.controller;

import br.com.db1.demo.dto.PersonDTO;
import br.com.db1.demo.model.Person;
import br.com.db1.demo.repository.PersonRepository;
import br.com.db1.demo.service.PersonPersistService;
import br.com.db1.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/people/history")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonPersistService personPersistService;


    @GetMapping("/record")
    public List<Person> getPeople() {
        return personService.getPeople();
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personService.getPerson(id);
    }


    @PostMapping("/person")
    public ResponseEntity<Object> createPerson(@RequestBody PersonDTO body) {
        Person savedPerson = personPersistService.insertPerson(body);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPerson.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    @PutMapping("/person/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody PersonDTO body, @PathVariable long id) {
        personPersistService.updatePerson(body, id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/person/{id}")
    public void deletePerson(@PathVariable long id) {
        personPersistService.deletePerson(id);
    }
}


