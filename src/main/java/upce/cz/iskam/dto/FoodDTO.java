package upce.cz.iskam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
    private String image;
    private List<Long> ingredients;

}
