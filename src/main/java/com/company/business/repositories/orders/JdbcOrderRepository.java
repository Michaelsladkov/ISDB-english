package com.company.business.repositories.orders;

import com.company.business.models.Order;
import com.company.business.repositories.people.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcOrderRepository.class);
  private final static RowMapper<DbOrder> dbOrderRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    int customerId = rs.getInt("customer_id");
    LocalDateTime time = rs.getTimestamp("time").toLocalDateTime();
    boolean isClosed = rs.getBoolean("closed");
    return new DbOrder(id, customerId, time, isClosed);
  };
  private final static String GET_BY_ID_QUERY =
    "select id, customer_id, time, closed from orders where id = ?";
  private final static String INSERT_QUERY =
    "insert into orders (customer_id, time, closed) values (?, ?, ?) returning id";
  private final static String SET_CLOSED_QUERY =
    "update orders set closed = true where id = ?";
  private final CustomerRepository customerRepository;
  private final JdbcTemplate jdbcTemplate;

  public JdbcOrderRepository(JdbcTemplate jdbcTemplate, CustomerRepository customerRepository) {
    this.jdbcTemplate = jdbcTemplate;
    this.customerRepository = customerRepository;
  }

  @Override
  public Order get(int id) {
    List<DbOrder> dbOrderByList = jdbcTemplate.query(GET_BY_ID_QUERY, dbOrderRowMapper, id);
    if (dbOrderByList.isEmpty()) {
      logger.error("Can't find order by id = '" + id + "'");
      return null;
    }

    var dbOrder = dbOrderByList.get(0);
    var customer = customerRepository.getById(dbOrder.customerId);
    if (customer == null)
      return null;

    return new Order(dbOrder.id, customer, dbOrder.time, dbOrder.closed);
  }

  @Override
  public int add(Order order) {
    Integer id = jdbcTemplate.queryForObject(
      INSERT_QUERY, Integer.class, order.getCustomer().getId(), order.getTime(), order.isClosed()
    );

    if (id == null) {
      // TODO
      throw new IllegalArgumentException("Stub");
    }

    return id;
  }

  @Override
  public void setClosed(int id) {
    jdbcTemplate.update(SET_CLOSED_QUERY, id);
  }

  private static class DbOrder {
    int id;
    int customerId;
    LocalDateTime time;
    boolean closed;

    public DbOrder(int id, int customerId, LocalDateTime time, boolean closed) {
      this.id = id;
      this.customerId = customerId;
      this.time = time;
      this.closed = closed;
    }
  }
}
