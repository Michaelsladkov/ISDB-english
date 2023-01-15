package com.company.auth;

import org.springframework.stereotype.Service;

import static com.company.auth.UserService.CheckUserResult.*;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repository;

  public UserServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public User get(String login) {
    return repository.get(login);
  }

  @Override
  public void save(User user) {
    repository.add(user);
  }

  @Override
  public void update(User user) {
    repository.update(user);
  }

  @Override
  public CheckUserResult check(User user) {
    var userWithSameLogin = repository.get(user.getLogin());
    if (userWithSameLogin == null) {
      return NO_USER;
    }
    return user.getPassword().equals(userWithSameLogin.getPassword())
      ? SUCCESS
      : WRONG_PASSWORD;
  }
}
