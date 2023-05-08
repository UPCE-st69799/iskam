package upce.cz.iskam.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Order {
    @Id
    private Long id;

    @ManyToMany
    Set<Food> foodId;
}
