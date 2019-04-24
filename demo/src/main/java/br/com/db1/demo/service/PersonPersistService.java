package br.com.db1.demo.service;

import br.com.db1.demo.dto.PersonDTO;
import br.com.db1.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonPersistService {

    @Autowired
    PersonService personService;

    public Person insertPerson(PersonDTO dto) {
        Person person = new Person(dto.getName(), dto.getSurname(), dto.getComments());
        return personService.persistPerson(person);
    }

    public Person updatePerson(PersonDTO dto, Long id) {
        Person person = personService.getPerson(id);
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setComments(dto.getComments());
        return personService.persistPerson(person);

    }

    public void deletePerson(Long id) {
        Person person = personService.getPerson(id);
        personService.deletePerson(person);
    }

}
