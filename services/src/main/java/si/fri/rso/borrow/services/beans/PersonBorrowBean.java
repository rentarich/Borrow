package si.fri.rso.borrow.services.beans;

import si.fri.rso.borrow.models.entities.BorrowEntity;
import si.fri.rso.borrow.models.entities.ItemEntity;
import si.fri.rso.borrow.models.entities.PersonEntity;

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


    public BorrowEntity createPersonReserve(Integer itemId, Integer userId) throws ParseException {
        PersonEntity person = personBean.getPerson(userId);
        ItemEntity item = itemBean.getItem(itemId);
        return borrowBean.createReserve(person, item);
    }

    public BorrowEntity createPersonBorrow(Integer itemId, Integer userId) {
        PersonEntity person = personBean.getPerson(userId);
        ItemEntity item = itemBean.getItem(itemId);
        BorrowEntity entity= borrowBean.borrow(person,item);
        return entity;
    }

    public BorrowEntity returnItem(Integer itemId, Integer userId) {
        PersonEntity person = personBean.getPerson(userId);
        ItemEntity item = itemBean.getItem(itemId);
        BorrowEntity entity= borrowBean.returnItem(person,item);
        return entity;
    }
}
