package com.company.business.controllers;

import com.company.business.controllers.dto.FoodTypeDto;
import com.company.business.models.food.Food;
import com.company.business.models.food.FoodType;
import com.company.business.services.FoodService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
  private final FoodService foodService;

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
