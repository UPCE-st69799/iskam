package upce.cz.iskam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.entity.Ingredient;
import upce.cz.iskam.repository.FoodRepository;
import upce.cz.iskam.service.CategoryService;
import upce.cz.iskam.service.FoodService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appFood")
public class FoodController {
    private final FoodService foodService;
    private final CategoryService categoryService;


    public FoodController(FoodService foodService, CategoryService categoryService) {
        this.foodService = foodService;
        this.categoryService = categoryService;

    }

    @GetMapping("")
    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable("id") Long id) {
        Optional<Food> optionalFood = foodService.getFoodById(id);
        if (optionalFood.isPresent()) {
            return ResponseEntity.ok(optionalFood.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*@PostMapping("")
    public ResponseEntity<Food> createFood(@RequestBody Food food) {
        Food savedFood = foodService.createFood(food);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedFood.getId()).toUri();
        return ResponseEntity.created(location).body(savedFood);
    }*/


}

