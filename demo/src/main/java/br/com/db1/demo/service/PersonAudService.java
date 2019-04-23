package br.com.db1.demo.service;

import br.com.db1.demo.model.Person;
import br.com.db1.demo.repository.PersonRepository;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import java.util.List;

@Service
public class PersonAudService {

    private PersonRepository personRepository;

    @Autowired
    private EntityManager entityManager;

    public List getPersonRevisionsById(Long id) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(Person.class, true, true);
        auditQuery.add(AuditEntity.id().eq(id));
        return auditQuery.getResultList();
    }

    public List getRevisionsById(Integer rev) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery().forEntitiesAtRevision(Person.class,rev);
        auditQuery.add(AuditEntity.revisionNumber().eq(rev));
        return auditQuery.getResultList();
    }

}
