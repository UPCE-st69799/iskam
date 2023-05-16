package upce.cz.iskam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.AppUser;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {
   AppUser findByUsername(String username);
}