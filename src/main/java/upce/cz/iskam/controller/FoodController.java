package upce.cz.iskam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import upce.cz.iskam.component.ApiResponse;
import upce.cz.iskam.dto.FoodDTO;
import upce.cz.iskam.dto.FoodFilterRequest;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Food;
import upce.cz.iskam.entity.Ingredient;
import upce.cz.iskam.repository.FoodRepository;
import upce.cz.iskam.repository.IngredientRepository;
import upce.cz.iskam.service.CategoryService;
import upce.cz.iskam.service.FoodService;
import upce.cz.iskam.service.IngredientService;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
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
    private FoodRepository foodRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping("/head")
    public ResponseEntity<List<Food>> getFoods(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, name = "ingredientIds") List<Long> ingredientIds,
            @RequestParam(required = false, name = "ingredientIdsExclude") List<Long> ingredientIdsExclude,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Vytvoření objektu Pageable pro stránkování
        Pageable pageable = PageRequest.of(page, size);

        // Vytvoření objektu Specification pro filtry
        Specification<Food> specification = Specification.where(null);

        if (categoryId != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("category").get("id"), categoryId));
        }

        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            specification = specification.and((root, query, builder) ->
                    root.join("ingredients").get("id").in(ingredientIds));
        }

        if (ingredientIdsExclude != null && !ingredientIdsExclude.isEmpty()) {
            specification = specification.and((root, query, builder) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Food> subqueryRoot = subquery.from(Food.class);
                Join<Object, Object> subqueryJoin = subqueryRoot.join("ingredients");
                subquery.select(subqueryRoot.get("id"))
                        .where(builder.and(
                                builder.equal(subqueryRoot, root),
                                subqueryJoin.get("id").in(ingredientIdsExclude)
                        ));
                return builder.not(builder.exists(subquery));
            });
        }

        // Získání stránkovatelného seznamu jídel s použitím filtrů
        Page<Food> foodPage = foodRepository.findAll(specification, pageable);

        List<Food> foods = foodPage.getContent();
        int totalPages = foodPage.getTotalPages();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(foodPage.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(totalPages))
                .body(foods);
    }

    @PostMapping("/query")
    public ResponseEntity<List<Food>> getFoods(
            @RequestBody FoodFilterRequest filterRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Získání filtrů z requestu
        Long categoryId = filterRequest.getCategoryId();
        List<Long> ingredientIds = filterRequest.getIngredientIds();
        List<Long> ingredientIdsExclude = filterRequest.getIngredientIdsExclude();
        String name = filterRequest.getName();

        // Vytvoření objektu Pageable pro stránkování
        Pageable pageable = PageRequest.of(page, size);

        // Vytvoření objektu Specification pro filtry
        Specification<Food> specification = Specification.where(null);

        if (categoryId != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("category").get("id"), categoryId));
        }

        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            specification = specification.and((root, query, builder) ->
                    root.join("ingredients").get("id").in(ingredientIds));
        }

        if (ingredientIdsExclude != null && !ingredientIdsExclude.isEmpty()) {
            specification = specification.and((root, query, builder) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Food> subqueryRoot = subquery.from(Food.class);
                Join<Object, Object> subqueryJoin = subqueryRoot.join("ingredients");
                subquery.select(subqueryRoot.get("id"))
                        .where(builder.and(
                                builder.equal(subqueryRoot, root),
                                subqueryJoin.get("id").in(ingredientIdsExclude)
                        ));
                return builder.not(builder.exists(subquery));
            });
        }

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        // Získání stránkovatelného seznamu jídel s použitím filtrů
        Page<Food> foodPage = foodRepository.findAll(specification, pageable);

        List<Food> foods = foodPage.getContent();
        int totalPages = foodPage.getTotalPages();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(foodPage.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(totalPages))
                .body(foods);
    }


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
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody FoodDTO food) {
        Optional<Food> optionalFood = foodService.getFoodById(id);
        if (optionalFood.isPresent()) {
            Food existingFood = optionalFood.get();
            existingFood.setName(food.getName());
            existingFood.setDescription(food.getDescription());
            existingFood.setPrice(food.getPrice());
            existingFood.setImage(food.getImage());


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

