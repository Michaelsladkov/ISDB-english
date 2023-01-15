package com.company.business.models.people;

import java.util.Date;

public class Worker extends Person {
  private Profession profession;

  public Worker(int id, String name, Date birthday, int hp, int mana, int stamina, Profession profession) {
    super(id, name, birthday, hp, mana, stamina);
    this.profession = profession;
  }

  public Profession getProfession() {
    return profession;
  }

  public void setProfession(Profession profession) {
    this.profession = profession;
  }

  public enum Profession {
    WAITER
  }
}
