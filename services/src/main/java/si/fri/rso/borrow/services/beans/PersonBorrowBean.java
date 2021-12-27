package si.fri.rso.borrow.services.beans;

import si.fri.rso.borrow.models.entities.Borrow;
import si.fri.rso.borrow.models.entities.Item;
import si.fri.rso.borrow.models.entities.Person;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class PersonBorrowBean {
    private Logger log = Logger.getLogger(ItemBean.class.getName());
    private String idBean;

    @Inject
    PersonBean personBean;

    @Inject
    ItemBean itemBean;

    @Inject
    BorrowBean borrowBean;

    @PostConstruct
    private void init(){
        idBean = UUID.randomUUID().toString();
        log.info("Init bean: " + PersonBorrowBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PreDestroy
    private void destroy(){
        log.info("Deinit bean: " + PersonBorrowBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PersistenceContext(unitName = "item-jpa")
    private EntityManager em;


    public Borrow createPersonReserve(Integer itemId, Integer userId) throws ParseException {
        Person person = personBean.getPerson(userId);
        Item item = itemBean.getItem(itemId);
        return borrowBean.createReserve(person, item);
    }

    public Borrow createPersonBorrow(Integer itemId, Integer userId) {
        Person person = personBean.getPerson(userId);
        Item item = itemBean.getItem(itemId);
        Borrow entity= borrowBean.borrow(person,item);
        return entity;
    }

    public Borrow returnItem(Integer itemId, Integer userId) {
        Person person = personBean.getPerson(userId);
        Item item = itemBean.getItem(itemId);
        Borrow entity= borrowBean.returnItem(person,item);
        return entity;
    }
}
