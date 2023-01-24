package com.company.business.controllers;

import com.company.auth.SessionRepository;
import com.company.business.models.people.Customer;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.CustomerService;
import com.company.business.services.FoodService;
import com.company.business.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomersController extends BaseController {
  private final Logger logger = LoggerFactory.getLogger(CustomersController.class);
  private final CustomerService service;
  private final OrderService orderService;
  private final FoodService foodService;

  public CustomersController(SessionRepository sessionRepository, PeopleRepository peopleRepository, CustomerService service, OrderService orderService, FoodService foodService) {
    super(sessionRepository, peopleRepository);
    this.service = service;
    this.orderService = orderService;
    this.foodService = foodService;
  }

  @PostMapping("order")
  public String createOrder(HttpServletRequest request, Model model) {
    var customer = getCustomer();

    if (!validateBan(customer)) {
      logger.warn("Customer with id '" + customer.getId() + "' has been banned");
      return "banPage";
    }

    var foodName = request.getParameter("foodType");
    var count = Integer.parseInt(request.getParameter("count"));

    var order = orderService.correctOrder(customer, foodName, count);
    var details = orderService.getDetails(order.getId());

    model.addAllAttributes(Map.of(
      "order", order,
      "orderDetails", details
    ));

    return "redirect:/index";
  }

  private Customer getCustomer() {
    var person = getPerson();
    return service.get(person.getId());
  }

  private boolean validateBan(Customer customer) {
    return service.checkForBan(customer);
  }
}
