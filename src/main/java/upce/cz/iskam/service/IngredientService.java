package upce.cz.iskam.service;

import org.springframework.stereotype.Service;
import upce.cz.iskam.entity.Ingredient;
import upce.cz.iskam.repository.IngredientRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    @Transactional
    public List<Ingredient> getAllIngredients() {
        return (List<Ingredient>) ingredientRepository.findAll();
    }

    public Optional<Ingredient> getIngredientById(Long id) {
        return ingredientRepository.findById(id);
    }
    @Transactional
    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }
    @Transactional
    public Ingredient updateIngredient(Long id, Ingredient ingredient) {
        Optional<Ingredient> existingIngredient = ingredientRepository.findById(id);
        if (existingIngredient.isPresent()) {
            Ingredient updatedIngredient = existingIngredient.get();
            updatedIngredient.setName(ingredient.getName());
            updatedIngredient.setAllergen(ingredient.getAllergen());
            return ingredientRepository.save(updatedIngredient);
        }
        return null;
    }
    @Transactional
    public void deleteIngredient(Long id) {
        if (ingredientRepository.existsById(id)) {
            ingredientRepository.deleteById(id);
        }
    }
}
