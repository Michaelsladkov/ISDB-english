package com.company.business.repositories.orders;

import com.company.business.models.OrderDetails;
import com.company.business.repositories.food.FoodTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
class JdbcOrderDetailsRepository implements OrderDetailsRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcOrderDetailsRepository.class);
  private final static RowMapper<DbOrderDetails> dbOrderDetailsRowMapper = (rs, rowNum) -> {
    int orderId = rs.getInt("order_id");
    int food_id = rs.getInt("food_id");
    int count = rs.getInt("count");
    return new DbOrderDetails(orderId, food_id, count);
  };
  private final static String GET_BY_ORDER_ID_QUERY =
    "select order_id, food_id, count from order_details where order_id = ?";
  private final static String INSERT_QUERY =
    "insert into order_details (order_id, food_id, count) values (?, ?, ?)";

  private final FoodTypeRepository foodTypeRepository;
  private final JdbcTemplate jdbcTemplate;

  JdbcOrderDetailsRepository(FoodTypeRepository foodTypeRepository, JdbcTemplate jdbcTemplate) {
    this.foodTypeRepository = foodTypeRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<OrderDetails> get(int orderId) {
    var dbOrderDetails = jdbcTemplate.query(GET_BY_ORDER_ID_QUERY, dbOrderDetailsRowMapper);

    var foodTypeById = dbOrderDetails.stream()
      .map(DbOrderDetails::getFood_id)
      .distinct()
      .collect(Collectors.toMap(
        Function.identity(),
        foodTypeRepository::get
      ));

    return dbOrderDetails.stream()
      .map(dbDetails -> {
        var foodType = foodTypeById.get(dbDetails.food_id);
        return new OrderDetails(foodType, dbDetails.count);
      })
      .collect(Collectors.toList());
  }

  @Override
  public void add(int orderId, OrderDetails details) {
    jdbcTemplate.update(INSERT_QUERY, orderId, details.getFoodType().getId(), details.getCount());
  }

  @Override
  public void add(int orderId, List<OrderDetails> details) {
    var batchArgs = details.stream().map(d ->
      new Object[]{orderId, d.getFoodType().getId(), d.getCount()}
    ).collect(Collectors.toList());

    jdbcTemplate.batchUpdate(INSERT_QUERY, batchArgs);
  }

  private static class DbOrderDetails {
    int orderId;
    int food_id;
    int count;

    public DbOrderDetails(int orderId, int food_id, int count) {
      this.orderId = orderId;
      this.food_id = food_id;
      this.count = count;
    }

    public int getFood_id() {
      return food_id;
    }
  }
}
