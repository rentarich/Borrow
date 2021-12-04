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
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
        TypedQuery<BorrowEntity> query= em.createNamedQuery("Borrow.getReservedOrBorrowedItems",BorrowEntity.class);
        List<ItemEntity> itemsBor = new ArrayList<>();
        for (BorrowEntity borrow : query.getResultList()) {
            itemsBor.add(borrow.getItem());
        }
        return itemsBor;
    }

    @Transactional
    public BorrowEntity createItem(BorrowEntity borrowEntity) {


        em.persist(borrowEntity);

        return borrowEntity;
    }

    public BorrowEntity putItem(boolean returned, BorrowEntity item) {

        BorrowEntity c = em.find(BorrowEntity.class, item.getId());

        if (c == null) {
            return null;
        }

        c.setReturned(returned);
        c = em.merge(c);

        return c;
    }

    @Transactional
    public BorrowEntity createReserve(PersonEntity person, ItemEntity item) throws ParseException {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String from_date = dateFormat.format(date);
        System.out.println("Date before Addition: "+from_date);
        //Specifying date format that matches the given date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(from_date));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, 14);
        //Date after adding the days to the given date
        String returned_date = sdf.format(c.getTime());



        BorrowEntity borrow = new BorrowEntity();
        borrow.setFrom_date(from_date);
        borrow.setTo_date(returned_date);
        borrow.setReturned(true);
        borrow.setPerson(person);
        borrow.setItem(item);
        borrow.setReserved(true);

        em.persist(borrow);

        return borrow;
    }

    @Transactional
    public BorrowEntity borrow(PersonEntity person, ItemEntity item) {
        List itemEntities = em.createNamedQuery("Borrow.getPersonItem").setParameter("person",person).setParameter("item", item).getResultList();
        if(itemEntities.size()>0) {
            BorrowEntity e = (BorrowEntity) itemEntities.get(0);
            e.setReturned(false);
            em.merge(e);
            return e;
        }
        return new BorrowEntity();



    }

    @Transactional
    public BorrowEntity returnItem(PersonEntity person, ItemEntity item) {
        List itemEntities = em.createNamedQuery("Borrow.getPersonItem").setParameter("person",person).setParameter("item", item).getResultList();
        log.info(itemEntities.toString());
        if(itemEntities.size()>0) {
            BorrowEntity e = (BorrowEntity) itemEntities.get(0);
            log.info(e.getItem().getId().toString());
            log.info(e.getPerson().getId().toString());
            e.setReturned(true);
            e.setReserved(false);


            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String from_date = dateFormat.format(date);
            System.out.println("Date before Addition: "+from_date);
            //Specifying date format that matches the given date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try{
                //Setting the date to the given date
                c.setTime(sdf.parse(from_date));
            }catch(ParseException en){
                en.printStackTrace();
            }

            //Number of Days to add
            c.add(Calendar.DAY_OF_MONTH, -1);
            //Date after adding the days to the given date
            String returned_date = sdf.format(c.getTime());

            e.setTo_date(returned_date);
            em.merge(e);
            return e;
        }
        return new BorrowEntity();
    }
}