package upce.cz.iskam.controller;



import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upce.cz.iskam.entity.AppUser;
import upce.cz.iskam.entity.Role;
import upce.cz.iskam.service.UserService;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/app-user")
@RequiredArgsConstructor
public class UserController {
private final UserService userService;
    @GetMapping("/users")
public ResponseEntity<List<AppUser>> getUsers(){
    return ResponseEntity.ok().body(userService.getUsers());
}

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser user){
       URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveUser(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form){
        userService.addRoleToUser(form.getUsername(),form.getRoleName());
        return ResponseEntity.ok().build();
    }

}
@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}
