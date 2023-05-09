package upce.cz.iskam.service;

import org.springframework.stereotype.Service;
import upce.cz.iskam.entity.Category;
import upce.cz.iskam.entity.Ingredient;
import upce.cz.iskam.repository.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Transactional
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
    @Transactional
    public Category updateCategory(Long id, Category category) {
        Optional<Category> categoryToUpdate = categoryRepository.findById(id);
        if (categoryToUpdate.isPresent()) {
            category.setId(categoryToUpdate.get().getId());
            return categoryRepository.save(category);
        }
        return null;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Optional<Category> categoryToDelete = categoryRepository.findById(id);
        if (categoryToDelete.isPresent()) {
            categoryRepository.delete(categoryToDelete.get());
        }
    }
}


