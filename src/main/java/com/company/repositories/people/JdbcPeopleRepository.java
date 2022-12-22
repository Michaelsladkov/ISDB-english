package com.company.repositories.people;

import com.company.models.people.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class JdbcPeopleRepository implements PeopleRepository {
  private final JdbcTemplate jdbcTemplate;

  private final static RowMapper<Person> personRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    Date birthday = rs.getDate("birthday");
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    return new Person(id, name, birthday, hp, mana, stamina);
  };

  @Autowired
  public JdbcPeopleRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Person> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, personRowMapper);
  }

  @Override
  public Person getByName(String name) {
    return jdbcTemplate.query(GET_BY_NAME_QUERY, personRowMapper).get(0);
  }

  private static final String GET_ALL_QUERY =
    "select id, name, birthday, hp, mana, stamina from people";
  private static final String GET_BY_NAME_QUERY =
    "select id, name, birthday, hp, mana, stamina from people where name = ?";
}
