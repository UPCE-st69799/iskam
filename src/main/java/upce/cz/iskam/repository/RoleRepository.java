package upce.cz.iskam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upce.cz.iskam.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Any additional methods specific to Role entity can be added here

}

