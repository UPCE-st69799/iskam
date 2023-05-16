package upce.cz.iskam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByName(String role);
}

