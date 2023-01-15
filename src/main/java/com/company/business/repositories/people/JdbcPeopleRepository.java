package com.company.business.repositories.people;

import com.company.business.models.people.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class JdbcPeopleRepository implements PeopleRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcPeopleRepository.class);
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

  public JdbcPeopleRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Person> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, personRowMapper);
  }

  @Override
  public Person getById(int id) {
    List<Person> personByList = jdbcTemplate.query(GET_BY_ID_QUERY, personRowMapper, id);
    if (personByList.isEmpty()) {
      logger.error("Can't find person with id = '" + id + "'");
      return null;
    }
    return personByList.get(0);
  }

  @Override
  public Person getByName(String name) {
    List<Person> personByList = jdbcTemplate.query(GET_BY_NAME_QUERY, personRowMapper, name);
    if (personByList.isEmpty()) {
      logger.error("Can't find person by name = '" + name + "'");
      return null;
    }
    return personByList.get(0);
  }

  private static final String GET_ALL_QUERY =
    "select id, name, birthday, hp, mana, stamina from people";
  private static final String GET_BY_ID_QUERY =
    "select id, name, birthday, hp, mana, stamina from people where id = ?";
  private static final String GET_BY_NAME_QUERY =
    "select id, name, birthday, hp, mana, stamina from people where name = ?";
}
