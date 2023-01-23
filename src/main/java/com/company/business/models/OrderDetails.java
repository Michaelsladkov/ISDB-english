package com.company.business.models;

import com.company.business.models.food.FoodType;

public class OrderDetails {
  FoodType foodType;
  int count;

  public OrderDetails(FoodType foodType, int count) {
    this.foodType = foodType;
    this.count = count;
  }

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
}
