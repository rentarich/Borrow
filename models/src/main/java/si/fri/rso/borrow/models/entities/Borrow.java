package si.fri.rso.borrow.models.entities;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@Table(name="borrow")
@NamedQueries(value =
        {
                @NamedQuery(name = "Borrow.getAll", query = "SELECT b FROM Borrow b"),
                @NamedQuery(name = "Borrow.getBorrowForPerson", query = "SELECT b FROM Borrow b WHERE b.person = :person"),
                @NamedQuery(name = "Borrow.getBorrowForItem", query = "SELECT b FROM Borrow b WHERE b.item = :item"),
                @NamedQuery(name = "Borrow.getBorrowedItems", query = "SELECT b FROM Borrow b WHERE b.returned = false"),
                @NamedQuery(name = "Borrow.getReservedOrBorrowedItems", query = "SELECT b FROM Borrow b WHERE b.reserved = true OR b.returned=false"),
                @NamedQuery(name = "Borrow.getPersonItem", query = "SELECT b FROM Borrow b WHERE b.person = :person AND b.item =:item AND b.reserved =true")
        })

public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "from_date")
    private String from_date;

    @Column(name = "to_date")
    private String to_date;

    @Column(name= "reserved")
    private boolean reserved;

    @Column(name = "returned")
    private boolean returned;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "id_person")
    private Person person;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_item")
    private Item item;

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Item getItem(){
        return item;
    }

    public void setItem(Item item){
        this.item = item;
    }

    public Integer getId() {
        return id;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public void setReserved(boolean b) {
        this.reserved=b;
    }

    public boolean isReserved() {
        return reserved;
    }
}