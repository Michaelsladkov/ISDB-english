package com.company.business.services;

import com.company.business.models.Order;
import com.company.business.models.OrderDetails;
import com.company.business.repositories.orders.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
  private final OrderRepository repository;
  private final PeopleService peopleService;

  public OrderService(OrderRepository repository, PeopleService peopleService) {
    this.repository = repository;
    this.peopleService = peopleService;
  }

  public Order get(int id) {
    return repository.get(id);
  }

  public void closeOrder(Order order) {
    repository.setClosed(order.getId());

    var details = repository.getDetails(order.getId());
    int hpToIncrease = 0, manaToIncrease = 0, staminaToIncrease = 0;
    for (var d : details) {
      hpToIncrease += d.getFoodType().getHp() * d.getCount();
      manaToIncrease += d.getFoodType().getMana() * d.getCount();
      staminaToIncrease += d.getFoodType().getStamina() * d.getCount();
    }

    peopleService.increaseIndicators(
      order.getCustomer().getId(), hpToIncrease, manaToIncrease, staminaToIncrease
    );
  }
}
