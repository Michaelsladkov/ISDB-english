package com.company.business.controllers;

import com.company.auth.SessionRepository;
import com.company.business.models.Order;
import com.company.business.models.people.Customer;
import com.company.business.repositories.orders.OrderRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.CustomerService;
import com.company.business.services.FoodService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/customer")
public class CustomersController extends BaseController {
  private final Logger logger = LoggerFactory.getLogger(CustomersController.class);
  private final CustomerService service;
  private final OrderRepository orderRepository;
  private final FoodService foodService;

  public CustomersController(SessionRepository sessionRepository, PeopleRepository peopleRepository, CustomerService customerService, OrderRepository orderRepository, FoodService foodService) {
    super(sessionRepository, peopleRepository);
    this.service = customerService;
    this.orderRepository = orderRepository;
    this.foodService = foodService;
  }

  @PostMapping("order")
  public String createOrder(HttpServletRequest request, Model model) {
    var customer = getCustomer();

    if (!validateBan(customer)) {
      logger.warn("Customer with id '" + customer.getId() + "' has been banned");
      return "banPage";
    }

    var order = createNewEmptyOrder(customer);
    var foodName = request.getParameter("food_type");

    var details = foodService.getFoodTypes(Set.of(foodName));
    // TODO get details as List<> from request
    return "index";
  }

  private Order createNewEmptyOrder(Customer customer) {
    var order = new Order(null, customer, LocalDateTime.now(), false);
    Integer id = orderRepository.add(order);
    order.setId(id);
    return order;
  }

  private Customer getCustomer() {
    var person = getPerson();
    return service.get(person.getId());
  }

  private boolean validateBan(Customer customer) {
    return service.checkForBan(customer);
  }
}
