package com.company.business.repositories.food;

import com.company.business.models.food.Food;
import com.company.business.models.food.FoodType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcFoodStorageRepository implements FoodStorageRepository {
  private final static RowMapper<Food> foodRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    int price = rs.getInt("price");
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    var foodType = new FoodType(id, name, price, hp, mana, stamina);
    int count = rs.getInt("count");
    return new Food(foodType, count);
  };
  private final static String GET_ALL_QUERY = "select id, name, price, hp, mana, stamina, count" +
    " from food_storage as storage left join food_types as types on storage.food_type = types.id";
  private final static String GET_BY_ID_QUERY = "select id, name, price, hp, mana, stamina, count" +
    " from food_storage as storage left join food_types as types on storage.food_type = types.id" +
    " where storage.food_type = ?";
  private final static String INSERT_QUERY =
    "insert into food_storage (food_type, count) values(?, ?)";
  private final static String UPDATE_COUNT_QUERY =
    "update food_storage set count = ? where food_type = ?";

  private final JdbcTemplate jdbcTemplate;

  public JdbcFoodStorageRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Food> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, foodRowMapper);
  }

  @Override
  public Food get(int foodTypeId) {
    return jdbcTemplate.query(GET_BY_ID_QUERY, foodRowMapper, foodTypeId).get(0);
  }

  @Override
  public void save(Food food) {
    jdbcTemplate.update(INSERT_QUERY, food.getFoodType().getId(), food.getCount());
  }

  @Override
  public void save(List<Food> food) {
    var batchArgs = food.stream().map(f ->
      new Object[]{f.getFoodType().getId(), f.getCount()}
    ).toList();

    jdbcTemplate.batchUpdate(INSERT_QUERY, batchArgs);
  }

  @Override
  public void updateCount(Food food) {
    jdbcTemplate.update(UPDATE_COUNT_QUERY, food.getCount(), food.getFoodType().getId());
  }
}
