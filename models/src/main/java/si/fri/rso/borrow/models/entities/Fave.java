package si.fri.rso.borrow.models.entities;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@Table(name="favorites")
public class Fave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_item")
    private Item itemEntity;


    public Integer getId() {
        return id;
    }

    public Item getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(Item itemEntity) {
        this.itemEntity = itemEntity;
    }

    public Person getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(Person personEntity) {
        this.personEntity = personEntity;
    }

    @JsonbTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_person")
    private Person personEntity;

}
