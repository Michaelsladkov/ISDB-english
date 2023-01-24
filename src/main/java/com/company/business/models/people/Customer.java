package com.company.business.models.people;

import java.time.LocalDate;

public class Customer extends Person {
  private int loyaltyLevel;

  public Customer(int id, String name, LocalDate birthday, int hp, int mana, int stamina, int alcohol, int loyaltyLevel) {
    super(id, name, birthday, hp, mana, stamina, alcohol);
    this.loyaltyLevel = loyaltyLevel;
  }

  public Customer(Person person, int loyaltyLevel) {
    super(person.getId(), person.getName(), person.getBirthday(), person.getHp(), person.getMana(), person.getStamina(), person.getAlcohol());
    this.loyaltyLevel = loyaltyLevel;
  }

  public int getLoyaltyLevel() {
    return loyaltyLevel;
  }

  public void setLoyaltyLevel(int loyaltyLevel) {
    this.loyaltyLevel = loyaltyLevel;
  }
}
