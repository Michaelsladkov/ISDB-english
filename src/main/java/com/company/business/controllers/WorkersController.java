package com.company.business.controllers;

import com.company.auth.SessionRepository;
import com.company.business.models.people.Role;
import com.company.business.repositories.orders.OrderRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.OrderService;
import com.company.business.services.RolesHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("worker")
public class WorkersController extends BaseController {
  private static final Logger logger = LoggerFactory.getLogger(WorkersController.class);
  private final RolesHelper rolesHelper;
  private final OrderService orderService;

  public WorkersController(SessionRepository sessionRepository, PeopleRepository peopleRepository, RolesHelper rolesHelper, OrderService orderService) {
    super(sessionRepository, peopleRepository);
    this.rolesHelper = rolesHelper;
    this.orderService = orderService;
  }

  @PostMapping("/orders/close")
  public String closeOrder(HttpServletRequest request, Model model) {
    if (!validateRole(Role.ORDERS_MANAGER)) {
      model.addAttribute("error", true);
      return "index";
    }

    int orderId = Integer.parseInt(request.getParameter("order_id"));
    var order = orderService.get(orderId);

    orderService.closeOrder(order);

    return "index";
  }

  private boolean validateRole(Role expectedRole) {
    var person = getPerson();
    var roles = rolesHelper.getRoles(person);
    if (!roles.contains(expectedRole)) {
      logger.error("Person with id = '" + person.getId() + "' hasn't got permissions for '" + expectedRole + "' role");
      return false;
    }

    return true;
  }
}
