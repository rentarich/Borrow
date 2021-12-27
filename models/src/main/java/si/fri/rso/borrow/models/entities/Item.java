package si.fri.rso.borrow.models.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item")
@NamedQueries(value =
        {
                @NamedQuery(name = "ItemEntity.getAll",
                        query = "SELECT im FROM Item im")

        })
public class Item {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "title")
        private String title;

        @Column(name = "description")
        private String description;

        @Column(name = "category")
        private String category;

        @OneToMany(fetch = FetchType.EAGER)
        private List<Borrow> borrows;

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getCategory() {
                return category;
        }

        public void setCategory(String category) {
                this.category = category;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }
}
