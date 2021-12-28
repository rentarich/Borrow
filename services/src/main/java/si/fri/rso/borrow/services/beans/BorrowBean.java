package si.fri.rso.borrow.services.beans;


import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.enums.LogLevel;
import si.fri.rso.borrow.models.entities.Borrow;
import si.fri.rso.borrow.models.entities.Item;
import si.fri.rso.borrow.models.entities.Person;

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
    private com.kumuluz.ee.logs.Logger logger = LogManager.getLogger(BorrowBean.class.getName());
    private String idBean;

    @PostConstruct
    private void init(){
        idBean = UUID.randomUUID().toString();
        log.info("Init bean: " + BorrowBean.class.getSimpleName() + " idBean: " + idBean);
        logger.info("Init bean: " + BorrowBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PreDestroy
    private void destroy(){
        log.info("Deinit bean: " + BorrowBean.class.getSimpleName() + " idBean: " + idBean);
        logger.info("Deinit bean: " + BorrowBean.class.getSimpleName() + " idBean: " + idBean);
    }

    @PersistenceContext(unitName = "item-jpa")
    private EntityManager em;

    public List getBorrows(){
        return em.createNamedQuery("Borrow.getAll").getResultList();
    }

    public List<Borrow> getPersonsBorrows(Person person) {
        TypedQuery<Borrow> query= em.createNamedQuery("Borrow.getBorrowForPerson", Borrow.class);
        return query.setParameter("person",person).getResultList();
    }

    public List<Borrow> getItemBorrows(Item item) {
        TypedQuery<Borrow> query= em.createNamedQuery("Borrow.getBorrowForItem", Borrow.class);
        return query.setParameter("item",item).getResultList();
    }

    public List<Item> getBorrowedItems() {
        TypedQuery<Borrow> query= em.createNamedQuery("Borrow.getReservedOrBorrowedItems", Borrow.class);
        List<Item> itemsBor = new ArrayList<>();
        for (Borrow borrow : query.getResultList()) {
            itemsBor.add(borrow.getItem());
        }
        return itemsBor;
    }

    @Transactional
    public Borrow createItem(Borrow borrowEntity) {


        em.persist(borrowEntity);

        return borrowEntity;
    }

    public Borrow putItem(boolean returned, Borrow item) {

        Borrow c = em.find(Borrow.class, item.getId());

        if (c == null) {
            return null;
        }

        c.setReturned(returned);
        c = em.merge(c);

        return c;
    }

    @Transactional
    public Borrow createReserve(Person person, Item item) throws ParseException {
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



        Borrow borrow = new Borrow();
        borrow.setFrom_date(from_date);
        borrow.setTo_date(returned_date);
        borrow.setReturned(false);
        borrow.setPerson(person);
        borrow.setItem(item);
        borrow.setReserved(true);

        em.persist(borrow);

        return borrow;
    }

    @Transactional
    public Borrow borrow(Person person, Item item) {
        List itemEntities = em.createNamedQuery("Borrow.getPersonItem").setParameter("personid",person.getId()).setParameter("itemid", item.getId()).getResultList();
        if(itemEntities.size()>0) {
            Borrow e = (Borrow) itemEntities.get(0);
            e.setReturned(false);
            em.merge(e);
            return e;
        }
        return new Borrow();



    }

    @Transactional
    public Borrow returnItem(Integer personid, Integer itemId) {

        List itemEntities = em.createNamedQuery("Borrow.getPersonItem").setParameter("personid",personid).setParameter("itemid", itemId).getResultList();
        log.info(itemEntities.toString());
        logger.info("Retruning item");
        if(itemEntities.size()>0) {
            Borrow borrow =null;
            List<Borrow> e = (List<Borrow>) itemEntities;
            for (Borrow b :e) {
                if(!b.isReturned()) {
                    borrow = b;
                    log.info(b.getItem().getId().toString());
                    log.info(b.getPerson().getId().toString());
                    b.setReturned(true);
                    b.setReserved(false);
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String from_date = dateFormat.format(date);
                    System.out.println("Date before Addition: " + from_date);
                    //Specifying date format that matches the given date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    try {
                        //Setting the date to the given date
                        c.setTime(sdf.parse(from_date));
                    } catch (ParseException en) {
                        en.printStackTrace();
                    }

                    //Number of Days to add
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    //Date after adding the days to the given date
                    String returned_date = sdf.format(c.getTime());

                    b.setTo_date(returned_date);
                    Borrow borrows = new Borrow();
                    borrows.setFrom_date(from_date);
                    borrows.setTo_date(returned_date);
                    borrows.setReturned(true);
                    borrows.setPerson(b.getPerson());
                    borrows.setItem(b.getItem());
                    borrows.setReserved(false);
                    em.remove(b);
                    em.merge(borrows);
                }
            }
            return borrow;
        }
        return new Borrow();
    }
}