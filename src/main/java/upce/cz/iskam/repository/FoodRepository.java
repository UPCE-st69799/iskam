package upce.cz.iskam.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Food;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends PagingAndSortingRepository<Food,Long> {
    List<Food> findAll();
    Optional<Food> findById(Long id);
    Food save(Food food);
}

