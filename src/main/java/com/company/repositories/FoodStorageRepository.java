package com.company.repositories;

import com.company.models.food.Food;

import java.util.List;

public interface FoodStorageRepository {
  List<Food> getAll();

  Food get(int id);
}
