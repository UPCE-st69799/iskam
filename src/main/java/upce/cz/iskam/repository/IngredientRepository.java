package upce.cz.iskam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Ingredient;
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{

}
