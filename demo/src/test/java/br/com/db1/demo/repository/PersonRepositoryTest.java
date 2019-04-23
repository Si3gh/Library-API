package br.com.db1.demo.repository;

import br.com.db1.demo.model.Person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


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

    @Test
    public void testVertical_getChanges() {

    }
}
