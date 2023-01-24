package com.company.business.repositories.orders;

import com.company.business.models.Order;
import com.company.business.models.OrderDetails;
import com.company.business.models.people.Customer;
import com.company.business.repositories.people.CustomerRepository;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
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
  private final static String GET_ALL_OPEN_QUERY =
    "select id, customer_id, time, closed from orders where close = false";
  private final static String GET_OPEN_BY_CUSTOMER_ID_QUERY =
    "select id, customer_id, time, closed from orders where customer_id = ? and closed = false";
  private final static String INSERT_QUERY =
    "insert into orders (customer_id, time, closed) values (?, ?, ?) returning id";
  private final static String SET_CLOSED_QUERY =
    "update orders set closed = true where id = ?";
  private final CustomerRepository customerRepository;
  private final JdbcTemplate jdbcTemplate;
  private final OrderDetailsRepository orderDetailsRepository;

  public JdbcOrderRepository(CustomerRepository customerRepository, JdbcTemplate jdbcTemplate, OrderDetailsRepository orderDetailsRepository) {
    this.customerRepository = customerRepository;
    this.jdbcTemplate = jdbcTemplate;
    this.orderDetailsRepository = orderDetailsRepository;
  }

  @Override
  public Order get(int id) {
    List<DbOrder> dbOrderByList = jdbcTemplate.query(GET_BY_ID_QUERY, dbOrderRowMapper, id);
    if (dbOrderByList.isEmpty()) {
      logger.warn("Can't find order by id = '" + id + "'");
      return null;
    }

    var dbOrder = dbOrderByList.get(0);
    var customer = customerRepository.get(dbOrder.customerId);
    if (customer == null)
      return null;

    return new Order(dbOrder.id, customer, dbOrder.time, dbOrder.closed);
  }

  @Override
  public List<Order> getAllOpen() {
    List<DbOrder> dbOrderByList = jdbcTemplate.query(GET_ALL_OPEN_QUERY, dbOrderRowMapper);
    return EntryStream.of(StreamEx.of(dbOrderByList).groupingBy(DbOrder::getCustomerId))
      .mapKeys(customerRepository::get)
      .flatMapKeyValue((customer, dbOrders) ->
        StreamEx.of(dbOrders).map(order -> new Order(order.id, customer, order.time, true))
      )
      .toList();
  }

  @Override
  public Order getOpen(Customer customer) {
    List<DbOrder> dbOrderByList = jdbcTemplate.query(GET_OPEN_BY_CUSTOMER_ID_QUERY, dbOrderRowMapper, customer.getId());
    if (dbOrderByList.isEmpty()) {
      logger.warn("Can't find open order by customer id = '" + customer.getId() + "'");
      return null;
    }

    var dbOrder = dbOrderByList.get(0);

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

  @Override
  public List<OrderDetails> getDetails(int id) {
    return orderDetailsRepository.get(id);
  }

  @Override
  public void addDetails(int id, List<OrderDetails> details) {
    orderDetailsRepository.add(id, details);
  }

  @Override
  public void updateCount(int id, OrderDetails details) {
    orderDetailsRepository.updateCount(id, details);
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

    public int getCustomerId() {
      return customerId;
    }
  }
}
