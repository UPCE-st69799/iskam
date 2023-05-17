package upce.cz.iskam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodFilterRequest {
    private Long categoryId;
    private List<Long> ingredientIds;
    private List<Long> ingredientIdsExclude;
    private String name;
    private String orderBy;

}
