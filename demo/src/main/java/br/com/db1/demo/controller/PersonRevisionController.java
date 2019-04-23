package br.com.db1.demo.controller;

import br.com.db1.demo.model.Person;
import br.com.db1.demo.service.PersonAudService;
import br.com.db1.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/people/history/")
public class PersonRevisionController {

    @Autowired
    private PersonAudService personAudService;

    @GetMapping("/person/revision/{id}")
    public List getPersonRevisionById(@PathVariable("id") Long id) {
        return personAudService.getPersonRevisionsById(id);
    }

    @GetMapping("/revision/{rev}")
    public List getRevisionById(@PathVariable("rev") Integer rev) {
        return personAudService.getRevisionsById(rev);
    }

}
