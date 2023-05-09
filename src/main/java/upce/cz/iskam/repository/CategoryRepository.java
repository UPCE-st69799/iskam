package upce.cz.iskam.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category,Long> {
    Optional<Category> findByNameIgnoreCase(String name);
}
