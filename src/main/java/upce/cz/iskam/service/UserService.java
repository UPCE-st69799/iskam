package upce.cz.iskam.service;


import upce.cz.iskam.entity.Role;
import upce.cz.iskam.entity.AppUser;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser appUser);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
}
