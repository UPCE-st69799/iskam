package upce.cz.iskam.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean allergen;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "ingredients",cascade = CascadeType.ALL)
    private List<Food> foods;


}

