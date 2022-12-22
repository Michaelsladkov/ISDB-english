package com.company.services;

import com.company.models.food.Food;
import com.company.repositories.FoodStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {
  private final FoodStorageRepository foodStorageRepository;

  @Autowired
  public FoodService(FoodStorageRepository foodStorageRepository) {
    this.foodStorageRepository = foodStorageRepository;
  }

  public List<Food> getAll() {
    return foodStorageRepository.getAll();
  }
}
