package com.company.business.repositories.orders;

import com.company.business.models.Order;
import com.company.business.models.OrderDetails;

import java.util.List;

public interface OrderRepository {
  Order get(int id);

  int add(Order order);

  void setClosed(int id);

  List<OrderDetails> getDetails(int id);

  void addDetails(int id, List<OrderDetails> details);
}
