package upce.cz.iskam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import upce.cz.iskam.entity.AppUser;
import upce.cz.iskam.entity.Role;
import upce.cz.iskam.service.UserService;

import java.util.ArrayList;

@SpringBootApplication
public class IskamApplication {

	public static void main(String[] args) {
		SpringApplication.run(IskamApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

/*	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null,"ROLE__USER"));
			userService.saveRole(new Role(null,"ROLE__ADMIN"));
			userService.saveRole(new Role(null,"ROLE__SUPER_ADMIN"));
			userService.saveRole(new Role(null,"ROLE__MANAGER"));

			userService.saveUser(new AppUser(null,"user5","password5",new ArrayList<>()));
			userService.saveUser(new AppUser(null,"user2","password2",new ArrayList<>()));
			userService.saveUser(new AppUser(null,"user3","password3",new ArrayList<>()));
			userService.saveUser(new AppUser(null,"user4","password4",new ArrayList<>()));

			userService.addRoleToUser("user5","ROLE_USER");
			userService.addRoleToUser("user5","ROLE__ADMIN");
			userService.addRoleToUser("user5","ROLE__SUPER_ADMIN");
			userService.addRoleToUser("user2","ROLE__MANAGER");
			userService.addRoleToUser("user3","ROLE_USER");
			userService.addRoleToUser("user4","ROLE_USER");


		};
	}*/
}
