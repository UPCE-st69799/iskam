package upce.cz.iskam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
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
import java.util.stream.Collectors;

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


    @PostMapping("/query")
    public ResponseEntity<List<Food>> getFoods(
            @RequestBody Optional<FoodFilterRequest> filterRequest,
            @RequestParam(defaultValue = "0") Optional<Integer> page,
            @RequestParam(defaultValue = "6") Optional<Integer> size
    ) {
        // Získání filtrů z requestu
        Long categoryId = filterRequest.map(FoodFilterRequest::getCategoryId).orElse(null);
        List<Long> ingredientIds = filterRequest.map(FoodFilterRequest::getIngredientIds).orElse(null);
        List<Long> ingredientIdsExclude = filterRequest.map(FoodFilterRequest::getIngredientIdsExclude).orElse(null);
        String name = filterRequest.map(FoodFilterRequest::getName).orElse(null);
        String orderBy = filterRequest.map(FoodFilterRequest::getOrderBy).orElse(null); // Získání parametru orderBy

        // Vytvoření objektu Pageable pro stránkování a řazení
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(6));

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

        // Přidání řazení do objektu Pageable
        if (orderBy != null && !orderBy.isEmpty()) {
            String[] orderByParts = orderBy.split(":");
            if (orderByParts.length == 2) {
                String sortField = orderByParts[0];
                String sortOrder = orderByParts[1];
                Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
                pageable = PageRequest.of(page.orElse(0), size.orElse(6), sort);
            }
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
    public ResponseEntity<List<Food>> getAllFoods(
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "0") int page) {
        List<Food> foods = foodService.getAllFoods();

        // Počet záznamů
        int totalCount = foods.size();

        // Stránkování
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // Oříznutí záznamů na aktuální stránku
        int fromIndex = Math.min(page * size, totalCount);
        int toIndex = Math.min(fromIndex + size, totalCount);
        List<Food> pagedFoods = foods.subList(fromIndex, toIndex);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(totalCount))
                .header("X-Total-Pages", String.valueOf(totalPages))
                .header("X-Actual-Pages", String.valueOf(page))
                .body(pagedFoods);
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
    public ResponseEntity<?> createFood(@RequestBody @Validated FoodDTO foodDTO) {
        try {
            // Check if a food with the same name already exists
            if (foodService.existsByName(foodDTO.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse(false, "Food with the same name already exists"));
            }

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

            return ResponseEntity.ok().body(new ApiResponse(true, "Food created successfully"));
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

            if (food.getName() != null) {
                existingFood.setName(food.getName());
            }
            if (food.getDescription() != null) {
                existingFood.setDescription(food.getDescription());
            }
            if (food.getPrice() != null) {
                existingFood.setPrice(food.getPrice());
            }
            if (food.getImage() != null) {
                existingFood.setImage(food.getImage());
            }
            if (food.getIngredients() != null) {
                List<Ingredient> ingredients = food.getIngredients().stream()
                        .map(ingredientId -> ingredientService.getIngredientById(ingredientId)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid ingredient ID: " + ingredientId)))
                        .collect(Collectors.toList());
                existingFood.setIngredients(ingredients);
            }

            Food updatedFood = foodService.updateFood(id,existingFood);
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

