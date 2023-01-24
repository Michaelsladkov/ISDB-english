package com.company.business.repositories.people;

import com.company.business.models.people.LoyaltyLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Supplier;

@Repository
public class JdbcLoyaltyLevelRepository implements LoyaltyLevelRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcLoyaltyLevelRepository.class);
  private final static RowMapper<LoyaltyLevel> loyaltyLevelRowMapper = (rs, rowNum) -> {
    int level = rs.getInt("level");
    int sale = rs.getInt("sale");
    String description = rs.getString("description");
    int money = rs.getInt("money");
    return new LoyaltyLevel(level, sale, description, money);
  };

  private static final String GET_ALL_QUERY =
    "select level, sale, description, money from loyalty_levels";
  private static final String GET_BY_LEVEL_QUERY =
    "select level, sale, description, money from loyalty_levels where level = ?";
  private static final String INSERT_QUERY =
    "insert into loyalty_levels (level, sale, description, money) values (?, ?, ?, ?)";

  private final JdbcTemplate jdbcTemplate;

  public JdbcLoyaltyLevelRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<LoyaltyLevel> get() {
    return jdbcTemplate.query(GET_ALL_QUERY, loyaltyLevelRowMapper);
  }

  @Override
  public LoyaltyLevel get(int level) {
    return wrapWithNullCheck(() -> jdbcTemplate.query(GET_BY_LEVEL_QUERY, loyaltyLevelRowMapper, level));
  }

  @Override
  public void save(LoyaltyLevel level) {
    jdbcTemplate.update(INSERT_QUERY, level.getLevel(), level.getSale(), level.getDescription(), level.getMoney());
  }

  private LoyaltyLevel wrapWithNullCheck(Supplier<List<LoyaltyLevel>> getter) {
    try {
      return getter.get().get(0);
    } catch (IndexOutOfBoundsException e) {
      logger.error("Can't find loyalty_level");
      return null;
    }
  }
}
