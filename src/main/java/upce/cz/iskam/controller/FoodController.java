package upce.cz.iskam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.repository.FoodRepository;

import java.util.List;

@RestController
@RequestMapping("/appFood")
public class FoodController {
    private final FoodRepository foodRepository;

    public FoodController(FoodRepository appUserRepository) {
        this.foodRepository = appUserRepository;
    }

    @GetMapping("")
    public Iterable<Food> findAll() {
        return foodRepository.findAll();
    }


}

