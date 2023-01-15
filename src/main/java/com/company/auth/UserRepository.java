package com.company.auth;

interface UserRepository {
  void add(User user);

  User get(String login);
}
