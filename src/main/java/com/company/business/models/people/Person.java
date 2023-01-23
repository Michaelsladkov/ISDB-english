package com.company.business.models.people;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Person {
  private Integer id;
  private String name;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthday;
  private int hp;
  private int mana;
  private int stamina;

  public Person(Integer id, String name, LocalDate birthday, int hp, int mana, int stamina) {
    this.id = id;
    this.name = name;
    this.birthday = birthday;
    this.hp = hp;
    this.mana = mana;
    this.stamina = stamina;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
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
