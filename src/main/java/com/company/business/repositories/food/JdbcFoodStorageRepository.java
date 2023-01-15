package com.company.business.repositories.food;

import com.company.business.models.food.Food;
import com.company.business.models.food.FoodType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcFoodStorageRepository implements FoodStorageRepository {
  private final JdbcTemplate jdbcTemplate;

  private final static RowMapper<Food> foodRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    var foodType = new FoodType(id, name, hp, mana, stamina);
    int count = rs.getInt("count");
    return new Food(foodType, count);
  };

  public JdbcFoodStorageRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Food> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, foodRowMapper);
  }

  @Override
  public Food get(int id) {
    return jdbcTemplate.query(GET_BY_ID_QUERY, foodRowMapper, id).get(0);
  }

  private final static String GET_ALL_QUERY = "select id, name, hp, mana, stamina, count" +
    " from food_storage as storage left join food_types as types on storage.food_type = types.id";

  private final static String GET_BY_ID_QUERY = "select id, name, hp, mana, stamina, count" +
    " from food_storage as storage left join food_types as types on storage.food_type = types.id" +
    " where storage.id = ?";
}
