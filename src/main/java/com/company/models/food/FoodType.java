package com.company.models.food;

public class FoodType {
  private Integer id;
  private String name;
  private int hp;
  private int mana;
  private int stamina;

  public Integer getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public int getMana() {
    return mana;
  }

  public void setMana(int mana) {
    this.mana = mana;
  }

  public int getStamina() {
    return stamina;
  }

  public void setStamina(int stamina) {
    this.stamina = stamina;
  }

  public FoodType(Integer id, String name, int hp, int mana, int stamina) {
    this.id = id;
    this.name = name;
    this.hp = hp;
    this.mana = mana;
    this.stamina = stamina;
  }
}
