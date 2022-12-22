package com.company.controllers;

import com.company.models.food.Food;
import com.company.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
}
