package com.company.auth;

public interface UserService {
  User get(String login);
  CheckUserResult check(User user);
  void update(User user);
  void save(User user);

  enum CheckUserResult {
    NO_USER,
    WRONG_PASSWORD,
    SUCCESS,
  }
}
