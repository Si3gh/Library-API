package br.com.demo.audit.controller;

import br.com.demo.audit.service.PersonAudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/people/")
public class PersonRevisionController {

    @Autowired
    private PersonAudService personAudService;

    @GetMapping("/history/{id}")
    public List getPersonRevisionById(@PathVariable("id") Long id) {
        return personAudService.getPersonRevisionsById(id);
    }

    @GetMapping("/revision/{rev}")
    public List getRevisionById(@PathVariable("rev") Integer rev) {
        return personAudService.getRevisionsByRev(rev);
    }

}
