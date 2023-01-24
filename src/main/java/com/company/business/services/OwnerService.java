package com.company.business.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
  private static final Logger logger = LoggerFactory.getLogger(OwnerService.class);

  private final JdbcTemplate jdbcTemplate;

  public OwnerService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void destroy() {
    jdbcTemplate.update(KILL_SCRIPT);
  }


  private static final String KILL_SCRIPT =
    "drop table ban_records cascade;" +
      "drop table workers cascade;" +
      "drop table mead_types cascade;" +
      "drop table food_storage cascade;" +
      "drop table order_details cascade;" +
      "drop table food_types cascade;" +
      "drop table orders cascade;" +
      "drop table customers cascade;" +
      "drop table loyalty_levels cascade;" +
      "drop table sessions cascade;" +
      "drop table users cascade;" +
      "drop table people cascade;" +
      "drop function count_spent_money_by_customer(integer) cascade;" +
      "drop function update_loyalty_level() cascade;" +
      "drop function reduce_food_count_in_storage() cascade;" +
      "drop function reduce_food_count_in_storage_by_diff() cascade;" +
      "drop function check_closed_from_false_to_true() cascade;";
}
