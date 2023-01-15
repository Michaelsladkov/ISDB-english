package com.company.controllers;

import com.company.controllers.dto.FoodTypeDto;
import com.company.models.food.Food;
import com.company.models.food.FoodType;
import com.company.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
  private final FoodService foodService;

  @Autowired
  public AdminController(FoodService service) {
    this.foodService = service;
  }

  @GetMapping("storage")
  List<Food> storage() {
    return foodService.getAll();
  }

  @PostMapping("storage/food-type")
  void addFoodType(@RequestBody FoodTypeDto request) {
    var foodTypeToAdd =
      new FoodType(null, request.name, request.hp, request.mana, request.stamina);
    foodService.addFoodType(foodTypeToAdd);
  }
}
