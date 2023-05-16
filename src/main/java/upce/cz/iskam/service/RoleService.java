package upce.cz.iskam.service;

import org.springframework.stereotype.Service;
import upce.cz.iskam.entity.Role;
import upce.cz.iskam.repository.RoleRepo;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role createRole(Role role) {
        return roleRepo.save(role);
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepo.findById(id);
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public void deleteRoleById(Long id) {
        roleRepo.deleteById(id);
    }

    public void updateRole(Role role) {
        roleRepo.save(role);
    }
}
