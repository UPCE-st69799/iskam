package upce.cz.iskam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.entity.Ingredient;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long>, JpaSpecificationExecutor<Food> {

    List<Food> findByCategory(Category category);
    List<Food> findByIngredientsIn(List<Ingredient> ingredients);

    @Query("SELECT f FROM Food f WHERE NOT EXISTS (SELECT i FROM f.ingredients i WHERE i.id IN :ingredientIds)")
    Page<Food> findFoodsWithoutIngredients(@Param("ingredientIds") List<Long> ingredientIds, Pageable pageable);

}

