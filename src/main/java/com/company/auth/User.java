package com.company.auth;

public class User {
  private String login;
  private String password;
  private Integer personId;

  public User(String login, String password, Integer personId) {
    this.login = login;
    this.password = password;
    this.personId = personId;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getPersonId() {
    return personId;
  }

  public void setPersonId(Integer personId) {
    this.personId = personId;
  }
}
