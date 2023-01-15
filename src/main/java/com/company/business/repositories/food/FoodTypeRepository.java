package com.company.business.repositories.food;

import com.company.business.models.food.FoodType;

import java.util.List;

public interface FoodTypeRepository {
  List<FoodType> getAll();

  FoodType getByName(String name);

  int save(FoodType foodType);
}
