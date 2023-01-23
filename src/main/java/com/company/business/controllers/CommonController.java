package com.company.business.controllers;

import com.company.auth.Session;
import com.company.auth.SessionRepository;
import com.company.auth.User;
import com.company.auth.UserService;
import com.company.business.models.food.FoodType;
import com.company.business.models.people.Customer;
import com.company.business.models.people.Person;
import com.company.business.repositories.food.FoodTypeRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.CustomerService;
import com.company.business.services.RolesHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

import static com.company.auth.UserService.CheckUserResult.NO_USER;
import static com.company.auth.UserService.CheckUserResult.SUCCESS;

@Controller
@RequestMapping("/")
public class CommonController extends BaseController {
  private final static Logger logger = LoggerFactory.getLogger(CommonController.class);
  private final UserService userService;
  private final FoodTypeRepository foodTypeRepository;
  private final CustomerService customerService;
  private final RolesHelper rolesHelper;

  public CommonController(SessionRepository sessionRepository, PeopleRepository peopleRepository, UserService userService, FoodTypeRepository foodTypeRepository, CustomerService customerService, RolesHelper rolesHelper) {
    super(sessionRepository, peopleRepository);
    this.userService = userService;
    this.foodTypeRepository = foodTypeRepository;
    this.customerService = customerService;
    this.rolesHelper = rolesHelper;
  }

  @GetMapping({"/", "index"})
  public String index(HttpServletRequest request, Model model) {
    var session = session();
    if (session == null)
      return "loginPage";

    var person = getPerson();
    var customer = customerService.get(person.getId());
    if (customer != null && !validateBan(customer)) {
      logger.warn("Person with id '" + customer.getId() + "' has been banned");
      return "ban";
    }

    model.addAttribute("person", person);
    model.addAttribute("Roles", rolesHelper);
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
    sessionRepository.delete(sessionId());
    return "redirect:/";
  }

  @GetMapping(value = "menu")
  public List<FoodType> menu() {
    return foodTypeRepository.getAll();
  }

  private Person personFromRequest(HttpServletRequest request) {
    return new Person(
      null, request.getParameter("name"),
      LocalDate.parse(request.getParameter("date")),
      Integer.parseInt(request.getParameter("hp")),
      Integer.parseInt(request.getParameter("mana")),
      Integer.parseInt(request.getParameter("stamina"))
    );
  }

  private boolean validateBan(Customer customer) {
    return customerService.checkForBan(customer);
  }
}
