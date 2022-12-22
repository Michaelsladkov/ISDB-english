package com.company.models.food;

public class Mead extends FoodType {
  private String sortName;
  private int intAlcohol;

  public String getSortName() {
    return sortName;
  }

  public void setSortName(String sortName) {
    this.sortName = sortName;
  }

  public int getIntAlcohol() {
    return intAlcohol;
  }

  public void setIntAlcohol(int intAlcohol) {
    this.intAlcohol = intAlcohol;
  }

  public Mead(int id, String name, int hp, int mana, int stamina, String sortName, int intAlcohol) {
    super(id, name, hp, mana, stamina);
    this.sortName = sortName;
    this.intAlcohol = intAlcohol;
  }
}
