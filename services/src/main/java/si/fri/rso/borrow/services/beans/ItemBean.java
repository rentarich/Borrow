package si.fri.rso.borrow.services.beans;

import si.fri.rso.borrow.models.dtos.Item;
import si.fri.rso.borrow.models.entities.ItemEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class ItemBean {
    private Logger log = Logger.getLogger(ItemBean.class.getName());
    private String idBean;

    @PostConstruct
    private void init(){
        idBean = UUID.randomUUID().toString();
        log.info("Init bean: " + ItemBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PreDestroy
    private void destroy(){
        log.info("Deinit bean: " + ItemBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PersistenceContext(unitName = "item-jpa")
    private EntityManager em;


    public ItemEntity getItem(int id) {
        ItemEntity item = em.find(ItemEntity.class, id);
        return item;
    }
}