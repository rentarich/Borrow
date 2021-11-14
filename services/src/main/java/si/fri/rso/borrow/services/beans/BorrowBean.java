package si.fri.rso.borrow.services.beans;


import si.fri.rso.borrow.models.entities.BorrowEntity;
import si.fri.rso.borrow.models.entities.ItemEntity;
import si.fri.rso.borrow.models.entities.PersonEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class BorrowBean {
    private Logger log = Logger.getLogger(BorrowBean.class.getName());
    private String idBean;

    @PostConstruct
    private void init(){
        idBean = UUID.randomUUID().toString();
        log.info("Init bean: " + BorrowBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PreDestroy
    private void destroy(){
        log.info("Deinit bean: " + BorrowBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PersistenceContext(unitName = "item-jpa")
    private EntityManager em;

    public List getBorrows(){
        return em.createNamedQuery("Borrow.getAll").getResultList();
    }

    public List<BorrowEntity> getPersonsBorrows(PersonEntity person) {
        TypedQuery<BorrowEntity> query= em.createNamedQuery("Borrow.getBorrowForPerson",BorrowEntity.class);
        return query.setParameter("person",person).getResultList();
    }

    public List<BorrowEntity> getItemBorrows(ItemEntity item) {
        TypedQuery<BorrowEntity> query= em.createNamedQuery("Borrow.getBorrowForItem",BorrowEntity.class);
        return query.setParameter("item",item).getResultList();
    }

    public List<ItemEntity> getBorrowedItems() {
        TypedQuery<BorrowEntity> query= em.createNamedQuery("Borrow.getBorrowedItems",BorrowEntity.class);
        List<ItemEntity> itemsBor = new ArrayList<>();
        for (BorrowEntity borrow : query.getResultList()) {
            itemsBor.add(borrow.getItem());
        }
        return itemsBor;
    }
}