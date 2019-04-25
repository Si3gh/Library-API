package br.com.db1.demo.controller;

import br.com.db1.demo.dto.PersonDTO;
import br.com.db1.demo.model.Person;
import br.com.db1.demo.service.PersonPersistService;
import br.com.db1.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// retirar history

@RestController
@RequestMapping("/api/people")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonPersistService personPersistService;

    // get all = /

    @GetMapping
    public List<Person> getPeople() {
        return personService.getPeople();
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personService.getPerson(id);
    }


    @PostMapping
    public ResponseEntity<Object> createPerson(@RequestBody PersonDTO body) {
        Person savedPerson = personPersistService.insertPerson(body);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPerson.getId()).toUri();

        return ResponseEntity.created(location).body(savedPerson);
    }


    @PutMapping("{id}")
    public ResponseEntity updatePerson(@RequestBody PersonDTO body, @PathVariable long id) {
        Person person = personPersistService.updatePerson(body, id);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("/person/{id}")
    public void deletePerson(@PathVariable long id) {
        personPersistService.deletePerson(id);
    }
}


