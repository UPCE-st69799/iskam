package upce.cz.iskam.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import upce.cz.iskam.controller.FoodController;
import upce.cz.iskam.dto.FoodDTO;
import upce.cz.iskam.entity.Food;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
public class FoodControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFoods() throws Exception {
        Food food1 = new Food();
        food1.setId(1L);
        food1.setName("Food 1");

        Food food2 = new Food();
        food2.setId(2L);
        food2.setName("Food 2");

        List<Food> foods = Arrays.asList(food1, food2);

        when(foodService.getAllFoods()).thenReturn(foods);

        mockMvc.perform(get("/appFood"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Food 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Food 2"));

        verify(foodService, times(1)).getAllFoods();
        verifyNoMoreInteractions(foodService);
    }

    @Test
    public void testGetFoodById() throws Exception {
        Food food = new Food();
        food.setId(1L);
        food.setName("Food");

        when(foodService.getFoodById(1L)).thenReturn(Optional.of(food));

        mockMvc.perform(get("/appFood/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Food"));

        verify(foodService, times(1)).getFoodById(1L);
        verifyNoMoreInteractions(foodService);
    }

    @Test
    public void testCreateFood() throws Exception {
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setName("New Food");

        Food createdFood = new Food();
        createdFood.setId(1L);
        createdFood.setName("New Food");

        when(foodService.existsByName("New Food")).thenReturn(false);
        when(foodService.createFood(any(Food.class), anyList())).thenReturn(createdFood);

        ObjectMapper objectMapper = new ObjectMapper();
        String foodJson = objectMapper.writeValueAsString(foodDTO);

        mockMvc.perform(post("/appFood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(foodJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Food"));

        verify(foodService, times(1)).existsByName("New Food");
        verify(foodService, times(1)).createFood(any(Food.class), anyList());
        verifyNoMoreInteractions(foodService);
    }

    @Test
    public void testUpdateFood() throws Exception {
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setName("Updated Food");

        Food existingFood = new Food();
        existingFood.setId(1L);
        existingFood.setName("Existing Food");

        Food updatedFood = new Food();
        updatedFood.setId(1L);
        updatedFood.setName("Updated Food");

        when(foodService.getFoodById(1L)).thenReturn(Optional.of(existingFood));
        when(foodService.updateFood(1L, existingFood)).thenReturn(updatedFood);

        ObjectMapper objectMapper = new ObjectMapper();
        String foodJson = objectMapper.writeValueAsString(foodDTO);

        mockMvc.perform(put("/appFood/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(foodJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Food"));

        verify(foodService, times(1)).getFoodById(1L);
        verify(foodService, times(1)).updateFood(1L, existingFood);
        verifyNoMoreInteractions(foodService);
    }

    @Test
    public void testDeleteFood() throws Exception {
        when(foodService.getFoodById(1L)).thenReturn(Optional.of(new Food()));

        mockMvc.perform(delete("/appFood/1"))
                .andExpect(status().isNoContent());

        verify(foodService, times(1)).getFoodById(1L);
        verify(foodService, times(1)).deleteFood(1L);
        verifyNoMoreInteractions(foodService);
    }

    @Test
    public void testCreateExistingFood() throws Exception {
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setName("Existing Food");

        when(foodService.existsByName("Existing Food")).thenReturn(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String foodJson = objectMapper.writeValueAsString(foodDTO);

        mockMvc.perform(post("/appFood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(foodJson))
                .andExpect(status().isBadRequest());

        verify(foodService, times(1)).existsByName("Existing Food");
        verifyNoMoreInteractions(foodService);
    }

}
