package upce.cz.iskam.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Food;

import java.util.List;

@Repository
public interface FoodRepository extends PagingAndSortingRepository<Food,Long> {
    Iterable<Food> findAll();
}

