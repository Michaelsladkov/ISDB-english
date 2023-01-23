package com.company.business.repositories.food;

import com.company.business.models.food.Food;
import com.company.business.models.food.FoodType;

import java.util.List;

public interface FoodStorageRepository {
  List<Food> getAll();

  Food get(int foodTypeId);

  void save(Food food);

  void save(List<Food> food);

  void updateCount(Food food);
}
