package si.fri.rso.borrow.models.entities;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@Table(name="favorites")
public class FaveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_item")
    private ItemEntity itemEntity;


    public Integer getId() {
        return id;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public PersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    @JsonbTransient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_person")
    private PersonEntity personEntity;

}
