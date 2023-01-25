package com.company.business.repositories.food;

import com.company.business.models.food.Mead;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcMeadRepository implements MeadRepository {
  private final static RowMapper<Mead> meadRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    int price = rs.getInt("price");
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    int alcohol = rs.getInt("alcohol");
    return new Mead(id, name, price, hp, mana, stamina, alcohol);
  };
  private final static String GET_ALL_QUERY =
    "select id, name, price, hp, mana, stamina, alcohol" +
      " from mead_types as meads left join food_types as types using(id)";
  private final static String INSERT_QUERY =
    "insert into mead_types (id, alcohol) values (?, ?)";
  private final FoodTypeRepository foodTypeRepository;
  private final JdbcTemplate jdbcTemplate;

  public JdbcMeadRepository(FoodTypeRepository foodTypeRepository, JdbcTemplate jdbcTemplate) {
    this.foodTypeRepository = foodTypeRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Mead> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, meadRowMapper);
  }

  @Override
  public int save(Mead mead) {
    int id = foodTypeRepository.save(mead);
    mead.setId(id);
    jdbcTemplate.update(INSERT_QUERY, mead.getId(), mead.getAlcohol());
    return id;
  }
}
