package upce.cz.iskam.component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import upce.cz.iskam.repository.FoodRepository;

@Component
@Slf4j // Annotation will create attribute log which can be used for formatted log-level logging
public class DatabaseRunner implements ApplicationRunner {
    private final FoodRepository foodRepository;

    public DatabaseRunner(FoodRepository appUserRepository) {
        this.foodRepository = appUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(ApplicationArguments args) {
        foodRepository.findAll().forEach(appUser -> log.info(appUser.toString()));
    }
}