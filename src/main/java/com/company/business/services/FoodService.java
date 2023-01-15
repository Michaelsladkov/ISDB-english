package com.company.business.services;

import com.company.business.models.food.Food;
import com.company.business.models.food.FoodType;
import com.company.business.repositories.food.FoodStorageRepository;
import com.company.business.repositories.food.FoodTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
