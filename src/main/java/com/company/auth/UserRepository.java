package com.company.auth;

import org.springframework.stereotype.Repository;

@Repository
interface UserRepository {
  void add(User user);

  User get(String login);
}
