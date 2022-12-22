package com.company.models.food;

public class Food {
  public FoodType getFoodType() {
    return foodType;
  }

  public void setFoodType(FoodType foodType) {
    this.foodType = foodType;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Food(FoodType foodType, int count) {
    this.foodType = foodType;
    this.count = count;
  }

  private FoodType foodType;
  private int count;
}
