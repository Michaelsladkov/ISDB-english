package com.company.controllers;

import com.company.models.food.FoodType;
import com.company.repositories.food.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class Controller {
  private final FoodTypeRepository foodTypeRepository;

  @Autowired
  public Controller(FoodTypeRepository foodTypeRepository) {
    this.foodTypeRepository = foodTypeRepository;
  }

  @GetMapping(path = "sir")
  public String sir() {
    return "SIR, YES SIR!";
  }

  @GetMapping(path = "menu")
  public List<FoodType> menu() {
    return foodTypeRepository.getAll();
  }
}
