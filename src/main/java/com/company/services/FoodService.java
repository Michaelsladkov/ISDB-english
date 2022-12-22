package com.company.services;

import com.company.models.food.Food;
import com.company.models.food.FoodType;
import com.company.repositories.food.FoodStorageRepository;
import com.company.repositories.food.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {
  private final FoodStorageRepository foodStorageRepository;
  private final FoodTypeRepository foodTypeRepository;

  @Autowired
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
