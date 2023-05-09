package upce.cz.iskam.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import upce.cz.iskam.entity.AppUser;
import upce.cz.iskam.service.AppUserService;
import upce.cz.iskam.service.ResourceNotFoundException;



@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("")
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
        AppUser newUser = appUserService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        AppUser user = appUserService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

