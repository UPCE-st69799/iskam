package upce.cz.iskam.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Data
@NoArgsConstructor
@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ToString.Exclude
    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String image;


    @ManyToMany
    @JoinTable(name = "food_ingredient",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))

    @ToString.Exclude // It will prevent to infinity loop in Lombok ToString generation because field from each class points to themselves
    @JsonIgnore
    private List<Ingredient> ingredients = new ArrayList<>();


}

