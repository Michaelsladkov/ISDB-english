package com.company.repositories;

import com.company.models.food.FoodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcFoodTypeRepository implements FoodTypeRepository {
  private final JdbcTemplate jdbcTemplate;

  private final static RowMapper<FoodType> foodTypeRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    return new FoodType(id, name, hp, mana, stamina);
  };

  @Autowired
  public JdbcFoodTypeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<FoodType> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, foodTypeRowMapper);
  }

  @Override
  public FoodType getByName(String name) {
    return jdbcTemplate.query(GET_BY_NAME_QUERY, foodTypeRowMapper, name).get(0);
  }

  private final static String GET_ALL_QUERY =
    "select id, name, hp, mana, stamina from food_types";

  private final static String GET_BY_NAME_QUERY =
    "select id, name, hp, mana, stamina from food_types where name = ?";
}
