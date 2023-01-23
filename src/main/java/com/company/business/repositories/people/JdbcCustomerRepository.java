package com.company.business.repositories.people;

import com.company.business.models.people.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcCustomerRepository implements CustomerRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
  private final static RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    LocalDate birthday = rs.getDate("birthday").toLocalDate();
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    int loyaltyLevel = rs.getInt("loyalty_level");
    return new Customer(id, name, birthday, hp, mana, stamina, loyaltyLevel);
  };
  private static final String GET_ALL_QUERY =
    "select c.id as id, name, birthday, hp, mana, stamina, loyalty_level " +
      "from customers as c inner join people as p using(id)";
  private static final String GET_BY_ID_QUERY =
    "select c.id, id, name, birthday, hp, mana, stamina, loyalty_level " +
      "from customers as c inner join people as p using(id) " +
      "where c.id = ?";
  private static final String INSERT_QUERY =
    "insert into customers (id, loyaltyLevel) values (?, ?)";
  private final JdbcTemplate jdbcTemplate;

  public JdbcCustomerRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Customer> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, customerRowMapper);
  }

  @Override
  public Customer get(int id) {
    List<Customer> customerByList = jdbcTemplate.query(GET_BY_ID_QUERY, customerRowMapper, id);
    if (customerByList.isEmpty()) {
      logger.error("Can't find customer with id = '" + id + "'");
      return null;
    }
    return customerByList.get(0);
  }

  @Override
  public void save(Customer customer) {
    jdbcTemplate.update(INSERT_QUERY, customer.getId(), customer.getLoyaltyLevel());
  }

}
