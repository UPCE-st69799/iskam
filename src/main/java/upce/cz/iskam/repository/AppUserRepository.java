package upce.cz.iskam.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.AppUser;

import java.util.List;

@Repository
public interface AppUserRepository extends PagingAndSortingRepository<AppUser, Long> {
    List<AppUser> findAllByActiveEquals(boolean active);
}