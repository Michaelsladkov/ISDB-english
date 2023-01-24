package com.company.business.repositories.people;

import com.company.business.models.people.Ban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Supplier;

@Repository
public class JdbcBanRepository implements BanRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcBanRepository.class);
  private final static RowMapper<Ban> banRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    int customerId = rs.getInt("customer_id");
    var from = rs.getDate("from").toLocalDate();
    var to = rs.getDate("to").toLocalDate();
    return new Ban(id, customerId, from, to);
  };

  private static final String GET_BY_ID_QUERY =
    "select id, customer_id, \"from\", \"to\" from ban_records where id = ?";
  private static final String GET_BY_CUSTOMER_ID_QUERY =
    "select id, customer_id, \"from\", \"to\" from ban_records where customer_id = ?";
  private static final String INSERT_QUERY =
    "insert into ban_records (customer_id, \"from\", \"to\") values (?, ?, ?) returning id";
  private static final String DELETE_QUERY =
    "delete from ban_records where customer_id = ?";

  private final JdbcTemplate jdbcTemplate;

  public JdbcBanRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Ban get(int id) {
    return wrapWithNullCheck(() ->
      jdbcTemplate.query(GET_BY_ID_QUERY, banRowMapper, id)
    );
  }

  @Override
  public Ban getByCustomerId(int customerId) {
    return wrapWithNullCheck(() ->
      jdbcTemplate.query(GET_BY_CUSTOMER_ID_QUERY, banRowMapper, customerId)
    );
  }

  @Override
  public Integer add(Ban ban) {
    return jdbcTemplate.queryForObject(INSERT_QUERY, Integer.class,
      ban.getCustomerId(), ban.getFrom(), ban.getTo()
    );
  }

  @Override
  public void delete(int customerId) {
    jdbcTemplate.update(DELETE_QUERY, customerId);
  }

  private Ban wrapWithNullCheck(Supplier<List<Ban>> getter) {
    try {
      return getter.get().get(0);
    } catch (IndexOutOfBoundsException e) {
      logger.info("Can't find ban");
      return null;
    }
  }
}
