package com.company.business.repositories.people;

import com.company.business.models.people.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcPeopleRepository implements PeopleRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcPeopleRepository.class);
  private final static RowMapper<Person> personRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    LocalDate birthday = rs.getDate("birthday").toLocalDate();
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    int alcohol = rs.getInt("alcohol");
    return new Person(id, name, birthday, hp, mana, stamina, alcohol);
  };
  private static final String GET_ALL_QUERY =
    "select id, name, birthday, hp, mana, stamina, alcohol from people";
  private static final String GET_BY_ID_QUERY =
    "select id, name, birthday, hp, mana, stamina, alcohol from people where id = ?";
  private static final String GET_BY_NAME_QUERY =
    "select id, name, birthday, hp, mana, stamina, alcohol from people where name = ?";
  private static final String INSERT_QUERY =
    "insert into people (name, birthday, hp, mana, stamina, alcohol) values (?, ?, ?, ?, ?, ?) returning id";
  private static final String UPDATE_INDICATORS_QUERY =
    "update people set hp = ?, mana = ?, stamina = ? where id = ?";
  private static final String UPDATE_ALCOHOL_QUERY =
    "update people set alcohol = ? where id = ?";
  private final JdbcTemplate jdbcTemplate;

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
      logger.warn("Can't find person with id = '" + id + "'");
      return null;
    }
    return personByList.get(0);
  }

  @Override
  public Person getByName(String name) {
    List<Person> personByList = jdbcTemplate.query(GET_BY_NAME_QUERY, personRowMapper, name);
    if (personByList.isEmpty()) {
      logger.warn("Can't find person by name = '" + name + "'");
      return null;
    }
    return personByList.get(0);
  }

  @Override
  public Integer save(Person person) {
    Integer id = jdbcTemplate.queryForObject(INSERT_QUERY, Integer.class,
      person.getName(), person.getBirthday(), person.getHp(), person.getMana(), person.getStamina()
    );

    if (id == null) {
      // TODO
      throw new IllegalArgumentException("Stub");
    }

    return id;
  }

  @Override
  public void updateIndicators(int id, int hp, int mana, int stamina) {
    jdbcTemplate.update(UPDATE_INDICATORS_QUERY, hp, mana, stamina, id);
  }
}
