package com.company.business.repositories.orders;

import com.company.business.models.OrderDetails;

import java.util.List;

interface OrderDetailsRepository {
  List<OrderDetails> get(int orderId);

  void add(int orderId, OrderDetails details);

  void add(int orderId, List<OrderDetails> details);
}
