package com.company.business.models.people;

public class LoyaltyLevel {
  private int level;
  private int sale;
  private String description;
  private int money;

  public LoyaltyLevel(int level, int sale, String description, int money) {
    this.level = level;
    this.sale = sale;
    this.description = description;
    this.money = money;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getSale() {
    return sale;
  }

  public void setSale(int sale) {
    this.sale = sale;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getMoney() {
    return money;
  }

  public void setMoney(int money) {
    this.money = money;
  }
}
