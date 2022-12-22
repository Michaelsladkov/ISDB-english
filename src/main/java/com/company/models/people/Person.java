package com.company.models.people;

import java.util.Date;

public class Person {
  private int id;
  private String name;
  private Date birthday;
  private int hp;
  private int mana;
  private int stamina;

  public Person(int id, String name, Date birthday, int hp, int mana, int stamina) {
    this.id = id;
    this.name = name;
    this.birthday = birthday;
    this.hp = hp;
    this.mana = mana;
    this.stamina = stamina;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
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
