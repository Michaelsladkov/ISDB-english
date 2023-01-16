package com.company.business.models.people;

import java.time.LocalDate;

public class Customer extends Person {
  int loyaltyLevel;

  public Customer(int id, String name, LocalDate birthday, int hp, int mana, int stamina, int loyaltyLevel) {
    super(id, name, birthday, hp, mana, stamina);
    this.loyaltyLevel = loyaltyLevel;
  }

  public int getLoyaltyLevel() {
    return loyaltyLevel;
  }

  public void setLoyaltyLevel(int loyaltyLevel) {
    this.loyaltyLevel = loyaltyLevel;
  }
}
