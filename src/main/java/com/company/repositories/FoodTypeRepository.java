package com.company.repositories;

import com.company.models.food.Food;
import com.company.models.food.FoodType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository {
  List<FoodType> getAll();

  FoodType getByName(String name);
}
