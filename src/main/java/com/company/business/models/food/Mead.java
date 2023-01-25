package com.company.business.models.food;

public class Mead extends FoodType {
  private int alcohol;

  public Mead(Integer id, String name, Integer price, int hp, int mana, int stamina, int alcohol) {
    super(id, name, price, hp, mana, stamina);
    this.alcohol = alcohol;
  }

  public int getAlcohol() {
    return alcohol;
  }

  public void setAlcohol(int alcohol) {
    this.alcohol = alcohol;
  }
}
