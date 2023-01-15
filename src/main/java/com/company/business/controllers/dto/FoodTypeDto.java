package com.company.business.controllers.dto;

public class FoodTypeDto {
  public String name;
  public int hp;
  public int mana;
  public int stamina;

  public FoodTypeDto(String name, int hp, int mana, int stamina) {
    this.name = name;
    this.hp = hp;
    this.mana = mana;
    this.stamina = stamina;
  }
}
