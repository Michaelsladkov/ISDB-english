package com.company.business.models;

import com.company.business.models.food.FoodType;

public class OrderDetails {
  Order order;
  FoodType foodType;
  int count;

  public OrderDetails(Order order, FoodType foodType, int count) {
    this.order = order;
    this.foodType = foodType;
    this.count = count;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
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
