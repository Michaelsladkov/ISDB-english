package com.company.business.controllers;

import com.company.auth.SessionRepository;
import com.company.business.models.food.FoodType;
import com.company.business.models.people.Role;
import com.company.business.repositories.people.LoyaltyLevelRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.FoodService;
import com.company.business.services.OrderService;
import com.company.business.services.RolesHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("worker")
public class WorkersController extends BaseController {
  private static final Logger logger = LoggerFactory.getLogger(WorkersController.class);
  private final RolesHelper rolesHelper;
  private final OrderService orderService;
  private final FoodService foodService;
  private final LoyaltyLevelRepository loyaltyLevelRepository;

  public WorkersController(SessionRepository sessionRepository, PeopleRepository peopleRepository, RolesHelper rolesHelper, OrderService orderService, FoodService foodService, LoyaltyLevelRepository loyaltyLevelRepository) {
    super(sessionRepository, peopleRepository);
    this.rolesHelper = rolesHelper;
    this.orderService = orderService;
    this.foodService = foodService;
    this.loyaltyLevelRepository = loyaltyLevelRepository;
  }

  @GetMapping("/orders")
  public String orders(HttpServletRequest request, Model model) {
    if (!validateRole(Role.ORDERS_MANAGER)) {
      return "redirect:/index";
    }

    var openOrders = orderService.getOpen();

    model.addAllAttributes(Map.of(
      "openOrders", openOrders
    ));

    return "cashierPage";
  }

  @GetMapping("/orders/details/{orderId}")
  public String orderDetails(HttpServletRequest request, Model model, @PathVariable Integer orderId) {
    if (!validateRole(Role.ORDERS_MANAGER)) {
      return "redirect:/index";
    }

    var order = orderService.get(orderId);
    var orderDetails = orderService.getDetails(orderId);
    var resultCost = orderService.countTotalCost(order);

    model.addAllAttributes(Map.of(
      "order", order,
      "orderDetails", orderDetails,
      "totalCost", resultCost,
      "loyaltyLevel", loyaltyLevelRepository.get(order.getCustomer().getLoyaltyLevel())
    ));

    return "orderDetailsPage";
  }

  @PostMapping("/orders/close")
  public String closeOrder(HttpServletRequest request, Model model) {
    if (!validateRole(Role.ORDERS_MANAGER)) {
      model.addAttribute("error", true);
      return "redirect:/index";
    }

    int orderId = Integer.parseInt(request.getParameter("orderId"));
    var order = orderService.get(orderId);

    orderService.closeOrder(order);

    return "cashierPage";
  }

  @GetMapping("/storage")
  public String storage(Model model) {
    if (!validateRole(Role.STORAGE_MANAGER)) {
      model.addAttribute("error", true);
      return "redirect:/index";
    }

    var foodTypes = foodService.getFoodTypes();
    var food = foodService.getAll();

    model.addAllAttributes(Map.of(
      "foodTypes", foodTypes,
      "storage", food
    ));

    return "warehousePage";
  }

  @PostMapping("/storage/food-types")
  public String addFoodType(HttpServletRequest request, Model model) {
    if (!validateRole(Role.STORAGE_MANAGER)) {
      model.addAttribute("error", true);
      return "index";
    }

    String foodTypeName = request.getParameter("name");
    int price = Integer.parseInt(request.getParameter("price"));
    int hp = Integer.parseInt(request.getParameter("hp"));
    int mana = Integer.parseInt(request.getParameter("mana"));
    int stamina = Integer.parseInt(request.getParameter("stamina"));

    var foodType = new FoodType(null, foodTypeName, price, hp, mana, stamina);

    foodService.addFoodType(foodType);

    return "redirect:/worker/storage";
  }

  @PostMapping("/storage")
  public String addFood(HttpServletRequest request, Model model) {
    if (!validateRole(Role.STORAGE_MANAGER)) {
      model.addAttribute("error", true);
      return "index";
    }

    String foodTypeName = request.getParameter("foodType");
    int count = Integer.parseInt(request.getParameter("count"));

    foodService.increaseFood(Map.of(foodTypeName, count));

    return "redirect:/worker/storage";
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
