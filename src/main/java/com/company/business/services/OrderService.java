package com.company.business.services;

import com.company.business.models.Order;
import com.company.business.models.OrderDetails;
import com.company.business.models.food.Mead;
import com.company.business.models.people.Customer;
import com.company.business.repositories.orders.OrderRepository;
import com.company.business.repositories.people.LoyaltyLevelRepository;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Service
public class OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
  private final OrderRepository repository;
  private final FoodService foodService;
  private final PeopleService peopleService;
  private final LoyaltyLevelRepository loyaltyLevelRepository;

  public OrderService(OrderRepository repository, FoodService foodService, PeopleService peopleService, LoyaltyLevelRepository loyaltyLevelRepository) {
    this.repository = repository;
    this.foodService = foodService;
    this.peopleService = peopleService;
    this.loyaltyLevelRepository = loyaltyLevelRepository;
  }

  public Order get(int id) {
    return repository.get(id);
  }

  public List<Order> getOpen() {
    return repository.getAllOpen();
  }

  public Order getOpen(Customer customer) {
    return repository.getOpen(customer);
  }

  public Order correctOrder(Customer customer, String foodTypeName, int count) {
    var order = repository.getOpen(customer);

    if (order == null) {
      order = createNewEmptyOrder(customer);
    }

    var foodType = foodService.getFoodTypes(Set.of(foodTypeName)).get(foodTypeName);
    var orderDetails = new OrderDetails(foodType, count);
    var existingDetails = StreamEx.of(repository.getDetails(order.getId()))
      .findFirst(details -> details.getFoodType().getName().equals(foodTypeName))
      .orElse(null);
    if (existingDetails != null) {
      existingDetails.setCount(existingDetails.getCount() + count);
      repository.updateCount(order.getId(), existingDetails);
    } else {
      repository.addDetails(order.getId(), List.of(orderDetails));
    }

    return order;
  }

  public List<OrderDetails> getDetails(int orderId) {
    return repository.getDetails(orderId);
  }

  public void closeOrder(Order order) {
    repository.setClosed(order.getId());

    var meads =
      StreamEx.of(foodService.getMeads()).toMap(Mead::getId, Function.identity());

    var details = repository.getDetails(order.getId());
    int hpToIncrease = 0, manaToIncrease = 0, staminaToIncrease = 0, alcoholToIncrease = 0;
    for (var d : details) {
      var foodType = d.getFoodType();
      hpToIncrease += foodType.getHp() * d.getCount();
      manaToIncrease += foodType.getMana() * d.getCount();
      staminaToIncrease += foodType.getStamina() * d.getCount();
      var foodAsMead = meads.getOrDefault(foodType.getId(), null);
      if (foodAsMead != null) {
        alcoholToIncrease += foodAsMead.getAlcohol() * d.getCount();
      }
    }

    peopleService.increaseIndicators(
      order.getCustomer().getId(), hpToIncrease, manaToIncrease, staminaToIncrease
    );
    peopleService.increaseAlcohol(order.getCustomer().getId(), alcoholToIncrease);
  }

  public double countTotalCost(Order order) {
    var details = repository.getDetails(order.getId());
    double costByDetails = StreamEx.of(details)
      .mapToInt(d -> d.getFoodType().getPrice() * d.getCount())
      .sum();

    var customer = order.getCustomer();
    double loyaltyLevelSale = loyaltyLevelRepository.get(customer.getLoyaltyLevel()).getSale();

    return costByDetails * (100. - loyaltyLevelSale) / 100.;
  }

  private Order createNewEmptyOrder(Customer customer) {
    var order = new Order(null, customer, LocalDateTime.now(), false);
    Integer id = repository.add(order);
    order.setId(id);
    return order;
  }
}
