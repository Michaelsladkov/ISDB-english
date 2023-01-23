package com.company.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcUserRepository.class);
  private final static RowMapper<User> userRowMapper = (rs, rowNum) -> {
    String login = rs.getString("login");
    String password = rs.getString("password");
    int personId = rs.getInt("person_id");
    return new User(login, password, personId);
  };
  private final static String GET_BY_LOGIN_QUERY =
    "select login, password, person_id from users where login = ?";
  private final static String INSERT_QUERY =
    "insert into users (login, password, person_id) values (?, ?, ?)";
  private final static String UPDATE_QUERY =
    "update users set login = ?, password = ?, person_id = ?";
  private final JdbcTemplate jdbcTemplate;

  public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public User get(String login) {
    List<User> userByList = jdbcTemplate.query(GET_BY_LOGIN_QUERY, userRowMapper, login);
    if (userByList.isEmpty()) {
      logger.warn("Cant find user with login = '" + login + "'");
      return null;
    }
    return userByList.get(0);
  }

  @Override
  public void add(User user) {
    jdbcTemplate.update(INSERT_QUERY, user.getLogin(), user.getPassword(), user.getPersonId());
  }

  @Override
  public void update(User user) {
    jdbcTemplate.update(UPDATE_QUERY, user.getLogin(), user.getPassword(), user.getPersonId());
  }
}
