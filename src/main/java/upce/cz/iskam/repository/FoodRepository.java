package upce.cz.iskam.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.entity.Ingredient;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends PagingAndSortingRepository<Food,Long> {

    List<Food> findByCategory(Category category);
    List<Food> findByIngredientsIn(List<Ingredient> ingredients);
}

