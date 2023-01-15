package com.company.business.repositories.food;

import com.company.business.models.food.Food;

import java.util.List;

public interface FoodStorageRepository {
  List<Food> getAll();

  Food get(int id);
}
