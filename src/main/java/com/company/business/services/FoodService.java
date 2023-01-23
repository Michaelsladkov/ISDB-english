package com.company.business.services;

import com.company.business.models.food.Food;
import com.company.business.models.food.FoodType;
import com.company.business.repositories.food.FoodStorageRepository;
import com.company.business.repositories.food.FoodTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FoodService {
  private static final Logger logger = LoggerFactory.getLogger(FoodService.class);
  private final FoodStorageRepository foodStorageRepository;
  private final FoodTypeRepository foodTypeRepository;

  public FoodService(
    FoodStorageRepository foodStorageRepository,
    FoodTypeRepository foodTypeRepository
  ) {
    this.foodStorageRepository = foodStorageRepository;
    this.foodTypeRepository = foodTypeRepository;
  }

  public List<FoodType> getFoodTypes() {
    return foodTypeRepository.getAll();
  }

  public List<Food> getAll() {
    return foodStorageRepository.getAll();
  }

  public int addFoodType(FoodType foodType) {
    return foodTypeRepository.save(foodType);
  }

  public Map<String, FoodType> getFoodTypes(Set<String> names) {
    return foodTypeRepository
      .get(names)
      .stream()
      .collect(Collectors.toMap(FoodType::getName, Function.identity()));
  }

  public void increaseFood(Map<String, Integer> increasesByFoodName) {
    var foodTypes = getFoodTypes(increasesByFoodName.keySet());

    increasesByFoodName
      .forEach((typeName, amount) -> {
        var type = foodTypes.get(typeName);
        var food = foodStorageRepository.get(type.getId());
        if (food == null) {
          food = new Food(type, amount);
          foodStorageRepository.save(food);
        } else {
          food.setCount(food.getCount() + amount);
          foodStorageRepository.updateCount(food);
        }
      });
  }
}
