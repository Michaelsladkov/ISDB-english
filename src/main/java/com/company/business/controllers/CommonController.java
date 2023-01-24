package com.company.business.controllers;

import com.company.auth.Session;
import com.company.auth.SessionRepository;
import com.company.auth.User;
import com.company.auth.UserService;
import com.company.business.models.food.FoodType;
import com.company.business.models.people.Customer;
import com.company.business.models.people.Person;
import com.company.business.repositories.people.LoyaltyLevelRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.company.auth.UserService.CheckUserResult.NO_USER;
import static com.company.auth.UserService.CheckUserResult.SUCCESS;

@Controller
@RequestMapping("/")
public class CommonController extends BaseController {
  private final static Logger logger = LoggerFactory.getLogger(CommonController.class);
  private final UserService userService;
  private final FoodService foodService;
  private final CustomerService customerService;
  private final RolesHelper rolesHelper;
  private final OrderService orderService;
  private final PeopleService peopleService;
  private final LoyaltyLevelRepository loyaltyLevelRepository;

  public CommonController(SessionRepository sessionRepository, PeopleRepository peopleRepository, UserService userService, FoodService foodService, CustomerService customerService, RolesHelper rolesHelper, OrderService orderService, PeopleService peopleService, LoyaltyLevelRepository loyaltyLevelRepository) {
    super(sessionRepository, peopleRepository);
    this.userService = userService;
    this.foodService = foodService;
    this.customerService = customerService;
    this.rolesHelper = rolesHelper;
    this.orderService = orderService;
    this.peopleService = peopleService;
    this.loyaltyLevelRepository = loyaltyLevelRepository;
  }

  @GetMapping({"/", "index"})
  public String index(HttpServletRequest request, Model model) {
    var session = session();
    if (session == null)
      return "loginPage";

    var person = getPerson();
    var customer = customerService.get(person.getId());
    if (customer == null) {
      return "loginPage";
    }

    if (!validateBan(customer)) {
      logger.warn("Person with id '" + customer.getId() + "' has been banned");
      return "banPage";
    }

    model.addAttribute("person", person);
    model.addAttribute("Roles", rolesHelper);

    var availableFoodTypes = new LinkedList<FoodType>();
    var foodTypesById = foodService.getFoodTypes()
      .stream()
      .collect(Collectors.toMap(FoodType::getId, Function.identity()));

    foodService.getAll().forEach(food -> {
      var foodType = food.getFoodType();
      if (foodTypesById.containsKey(foodType.getId())) {
        availableFoodTypes.add(foodType);
        foodTypesById.remove(foodType.getId());
      }
    });
    var notAvailableFoodTypes = foodTypesById.values();

    model.addAllAttributes(Map.of(
      "availableFoodTypes", availableFoodTypes,
      "notAvailableFoodTypes", notAvailableFoodTypes
    ));

    var order = orderService.getOpen(customer);
    if (order != null) {
      var details = orderService.getDetails(order.getId());
      model.addAllAttributes(Map.of(
        "order", order,
        "orderDetails", details,
        "totalCost", orderService.countTotalCost(order)
      ));
    }

    model.addAttribute("loyaltyLevel", loyaltyLevelRepository.get(customer.getLoyaltyLevel()));

    return "index";
  }

  @PostMapping(value = "sign-in")
  public String sigIn(HttpServletRequest request, Model model) {
    var login = (String) request.getParameter("login");
    var password = (String) request.getParameter("password");
    if (userService.check(new User(login, password, null)) != SUCCESS) {
      logger.error("Can't sign in with login = '" + login + "'");
      model.addAttribute("error", true);
      return "loginPage";
    }

    var user = userService.get(login);
    sessionRepository.store(new Session(sessionId(), user));

    return "redirect:/";
  }

  @GetMapping(value = "sign-up")
  public String registerUser() {
    return "registerPage";
  }

  @PostMapping(value = "sign-up")
  public String signUp(HttpServletRequest request, Model model) {
    var login = request.getParameter("login");
    var password = request.getParameter("password");
    var newUser = new User(login, password, null);
    var checkUserResult = userService.check(newUser);
    if (checkUserResult != NO_USER) {
      logger.error("User '" + login + "' is already exists");
      model.addAttribute("error", true);
      return "registerPage";
    }
    userService.save(newUser);
    sessionRepository.store(new Session(sessionId(), newUser));
    model.addAttribute("login", login);
    var person = personFromRequest(request);
    Integer personId = peopleRepository.save(person);
    person.setId(personId);

    newUser.setPersonId(personId);
    userService.update(newUser);

    customerService.addNewCustomer(person);

    return "redirect:/";
  }

  @PostMapping("/logout")
  public String logout() {
    peopleService.resetAlcohol(getPerson().getId());
    sessionRepository.delete(sessionId());
    return "redirect:/";
  }

  @GetMapping("/logout")
  public String logoutGet() {
    peopleService.resetAlcohol(getPerson().getId());
    sessionRepository.delete(sessionId());
    return "redirect:/";
  }

  private Person personFromRequest(HttpServletRequest request) {
    return new Person(
      null, request.getParameter("name"),
      LocalDate.parse(request.getParameter("date")),
      Integer.parseInt(request.getParameter("hp")),
      Integer.parseInt(request.getParameter("mana")),
      Integer.parseInt(request.getParameter("stamina")),
      0
    );
  }

  private boolean validateBan(Customer customer) {
    return customerService.checkForBan(customer);
  }
}
