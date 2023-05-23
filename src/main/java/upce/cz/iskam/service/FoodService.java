package upce.cz.iskam.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.entity.Ingredient;
import upce.cz.iskam.repository.CategoryRepository;
import upce.cz.iskam.repository.FoodRepository;
import upce.cz.iskam.repository.IngredientRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Food updateFood(Long id, Food updatedFood) {
        Optional<Food> existingFood = foodRepository.findById(id);
        if (existingFood.isEmpty()) {
            // Pokud jídlo s daným ID neexistuje, vyvoláme výjimku
            throw new EntityNotFoundException("Food with id " + id + " not found");
        }
        Food food = existingFood.get();
        food.setName(updatedFood.getName());
        food.setDescription(updatedFood.getDescription());
        food.setPrice(updatedFood.getPrice());
        food.setImage(updatedFood.getImage());
        food.setCategory(updatedFood.getCategory());
        food.setIngredients(updatedFood.getIngredients());
        // Není potřeba volat foodRepository.save(food), protože při ukončení transakce bude objekt food automaticky uložen
        // do databáze
        return food;
    }

    public boolean existsByName(String name) {
        return foodRepository.existsByName(name);
    }
    @Transactional
    public List<Food> getAllFoods() {
        return (List<Food>) foodRepository.findAll();
    }

    @Transactional
    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }

    @Transactional
    public Food createFood(Food food, List<Ingredient> resolvedIngredients) {
        // Najdeme kategorii jídla pomocí ID
        Category category = categoryRepository.findById(food.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        // Nastavíme kategorii jídlu
        food.setCategory(category);


        // Nastavíme ingredience jídlu
        food.setIngredients(resolvedIngredients);

        // Vytvoříme nové jídlo
        return foodRepository.save(food);
    }

    @Transactional
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    @Transactional
    public List<Food> getFoodsByCategory(Category category) {
        return foodRepository.findByCategory(category);
    }

    @Transactional
    public List<Food> findByCategory(Category category) {
        return foodRepository.findByCategory(category);
    }
}


