package br.com.demo.audit.repository;

import br.com.demo.audit.model.Person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;


    @Test
    public void shouldSavePerson() {
        Person person = new Person("Joao");
        personRepository.save(person);
        person.setComments("Test");
        personRepository.save(person);
        person.setSurname("Broietti");
        personRepository.save(person);


    }
}
