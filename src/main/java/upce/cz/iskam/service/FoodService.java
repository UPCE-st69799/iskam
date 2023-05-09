package upce.cz.iskam.service;

import org.springframework.stereotype.Service;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.entity.Ingredient;
import upce.cz.iskam.repository.FoodRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food createFood(String name, String description, Double price, Category category, String image, List<Ingredient> ingredients) {
        Food food = new Food();
        food.setName(name);
        food.setDescription(description);
        food.setPrice(price);
        food.setCategory(category);
        food.setImage(image);
        food.setIngredients(ingredients);
        return foodRepository.save(food);
    }
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }
}

