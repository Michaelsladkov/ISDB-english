package com.company.business.repositories.food;

import com.company.business.models.food.FoodType;

import java.util.List;
import java.util.Set;

public interface FoodTypeRepository {
  List<FoodType> getAll();

  FoodType get(String name);

  List<FoodType> get(Set<String> names);

  FoodType get(int id);

  int save(FoodType foodType);
}
