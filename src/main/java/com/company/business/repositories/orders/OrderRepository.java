package com.company.business.repositories.orders;

import com.company.business.models.Order;
import com.company.business.models.OrderDetails;
import com.company.business.models.people.Customer;

import java.util.List;

public interface OrderRepository {
  Order get(int id);

  List<Order> getAllOpen();

  Order getOpen(Customer customer);

  int add(Order order);

  void setClosed(int id);

  List<OrderDetails> getDetails(int id);

  void addDetails(int id, List<OrderDetails> details);

  void updateCount(int id, OrderDetails details);
}
