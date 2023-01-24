package com.company.business.models.food;

public class FoodType {
  private Integer id;
  private String name;
  private Integer price;
  private int hp;
  private int mana;
  private int stamina;

  public FoodType(Integer id, String name, Integer price, int hp, int mana, int stamina) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.hp = hp;
    this.mana = mana;
    this.stamina = stamina;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
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
}
