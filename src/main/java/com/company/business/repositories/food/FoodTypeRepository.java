package com.company.business.repositories.food;

import com.company.business.models.food.FoodType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository {
  List<FoodType> getAll();

  FoodType getByName(String name);

  int save(FoodType foodType);
}
