package br.com.db1.demo.service;

import br.com.db1.demo.model.Person;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class PersonAudService {

    @Autowired
    private EntityManager entityManager;

    public List getPersonRevisionsById(Long id) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery().forRevisionsOfEntity(Person.class, true, true);
        auditQuery.add(AuditEntity.id().eq(id));
        return auditQuery.getResultList();
    }

    public List getRevisionsByRev(Integer rev) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery().forEntitiesAtRevision(Person.class, rev);
        auditQuery.add(AuditEntity.revisionNumber().eq(rev));
        return auditQuery.getResultList();
    }

}
