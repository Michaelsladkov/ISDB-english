package com.company.business.services;

import com.company.business.models.food.Food;
import com.company.business.models.food.FoodType;
import com.company.business.repositories.food.FoodStorageRepository;
import com.company.business.repositories.food.FoodTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FoodService {
  private final FoodStorageRepository foodStorageRepository;
  private final FoodTypeRepository foodTypeRepository;

  public FoodService(
    FoodStorageRepository foodStorageRepository,
    FoodTypeRepository foodTypeRepository
  ) {
    this.foodStorageRepository = foodStorageRepository;
    this.foodTypeRepository = foodTypeRepository;
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
}
