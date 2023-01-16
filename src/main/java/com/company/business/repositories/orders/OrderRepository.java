package com.company.business.repositories.orders;

import com.company.business.models.Order;

public interface OrderRepository {
  Order get(int id);

  int add(Order order);

  void setClosed(int id);
}
