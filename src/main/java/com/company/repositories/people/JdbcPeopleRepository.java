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
    return new Person(id, name, birthday);
  };

  @Autowired
  public JdbcPeopleRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Person> getAll() {
    return null;
  }

  @Override
  public Person getByName(String name) {
    return null;
  }
}
