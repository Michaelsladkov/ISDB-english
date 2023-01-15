package com.company.controllers;

import com.company.models.food.FoodType;
import com.company.repositories.food.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class CommonController {
  private final FoodTypeRepository foodTypeRepository;

  @Autowired
  public CommonController(FoodTypeRepository foodTypeRepository) {
    this.foodTypeRepository = foodTypeRepository;
  }

  @GetMapping(path = "index")
  public String index(Model model) {
    return "redirect:/sir";
  }

  @GetMapping(path = "sir")
  public String sir(Model model) {
    model.getAttribute("A");
    model.addAttribute("A", "valla");

    return "abc";
  }

  @GetMapping(path = "menu")
  public List<FoodType> menu() {
    return foodTypeRepository.getAll();
  }
}
