package com.company.repositories.food;

import com.company.models.food.Mead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcMeadRepository implements MeadRepository {
  private final FoodTypeRepository foodTypeRepository;
  private final JdbcTemplate jdbcTemplate;

  private final static RowMapper<Mead> meadRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    String sortName = rs.getString("sort_name");
    int alcohol = rs.getInt("alcohol");
    return new Mead(id, name, hp, mana, stamina, sortName, alcohol);
  };

  @Autowired
  public JdbcMeadRepository(FoodTypeRepository foodTypeRepository, JdbcTemplate jdbcTemplate) {
    this.foodTypeRepository = foodTypeRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Mead> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, meadRowMapper);
  }

  @Override
  public Mead getByName(String name) {
    return jdbcTemplate.query(GET_BY_SORT_NAME_QUERY, meadRowMapper).get(0);
  }

  @Override
  public int save(Mead mead) {
    int id = foodTypeRepository.save(mead);
    mead.setId(id);
    jdbcTemplate.update(INSERT_QUERY, mead.getId(), mead.getSortName(), mead.getAlcohol());
    return id;
  }

  private final static String GET_ALL_QUERY =
    "select id, name, hp, mana, stamina, sort_name, alcohol" +
      " from mead_types as meads left join food_types as types using(id)";

  private final static String GET_BY_SORT_NAME_QUERY =
    "select id, name, hp, mana, stamina, sort_name, alcohol" +
      " from mead_types as meads left join food_types as types using(id)" +
      " where sort_name = ?";

  private final static String INSERT_QUERY =
    "insert into mead (id, sort_name, alcohol) values (?, ?, ?)";
}
