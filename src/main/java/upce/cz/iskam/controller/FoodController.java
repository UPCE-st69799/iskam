package upce.cz.iskam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upce.cz.iskam.component.ApiResponse;
import upce.cz.iskam.dto.FoodDTO;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.entity.Ingredient;
import upce.cz.iskam.repository.FoodRepository;
import upce.cz.iskam.service.CategoryService;
import upce.cz.iskam.service.FoodService;
import upce.cz.iskam.service.IngredientService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appFood")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IngredientService ingredientService;



    @GetMapping("")
    public ResponseEntity<List<Food>> getAllFoods() {
        List<Food> foods = foodService.getAllFoods();
        return ResponseEntity.ok(foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        Optional<Food> optionalFood = foodService.getFoodById(id);
        if (optionalFood.isPresent()) {
            Food food = optionalFood.get();
            return ResponseEntity.ok(food);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createFood(@RequestBody FoodDTO foodDTO) {
        try {
            // Get the category for the food
            Category category = categoryService.getCategoryById(foodDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

            // Get the ingredients for the food
            List<Ingredient> ingredients = new ArrayList<>();
            for (Long ingredientId : foodDTO.getIngredients()) {
                Ingredient ingredient = ingredientService.getIngredientById(ingredientId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid ingredient ID"));
                ingredients.add(ingredient);
            }

            // Create the new food
            Food food = new Food();
            food.setName(foodDTO.getName());
            food.setDescription(foodDTO.getDescription());
            food.setPrice(foodDTO.getPrice());
            food.setImage(foodDTO.getImage());
            food.setCategory(category);
            food.setIngredients(ingredients);

            // Save the new food to the database
            foodService.createFood(food, ingredients);

            return null;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to create food"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody Food food) {
        Optional<Food> optionalFood = foodService.getFoodById(id);
        if (optionalFood.isPresent()) {
            Food existingFood = optionalFood.get();
            existingFood.setName(food.getName());
            existingFood.setDescription(food.getDescription());
            existingFood.setPrice(food.getPrice());
            existingFood.setImage(food.getImage());
            existingFood.setCategory(food.getCategory());
            existingFood.setIngredients(food.getIngredients());

            Food updatedFood = foodService.updateFood(id, existingFood);
            return ResponseEntity.ok(updatedFood);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
       Optional<Food> optionalFood = foodService.getFoodById(id);
        if (optionalFood.isPresent()) {
            foodService.deleteFood(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

