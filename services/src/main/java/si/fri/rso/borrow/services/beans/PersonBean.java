package si.fri.rso.borrow.services.beans;

import si.fri.rso.borrow.models.entities.PersonEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class PersonBean {

    private Logger log = Logger.getLogger(PersonBean.class.getName());
    private String idBean;

    @PostConstruct
    private void init(){
        idBean = UUID.randomUUID().toString();
        log.info("Init bean: " + PersonBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PreDestroy
    private void destroy(){
        log.info("Deinit bean: " + PersonBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PersistenceContext(unitName = "item-jpa")
    private EntityManager em;

    public List getPersons(){

        return em.createNamedQuery("Person.getAll").getResultList();
    }

    public PersonEntity getPerson(int id) {
        PersonEntity person = em.find(PersonEntity.class, id);
        return person;
    }
}