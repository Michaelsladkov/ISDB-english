package com.company.business.controllers;

import com.company.auth.Session;
import com.company.auth.SessionRepository;
import com.company.auth.User;
import com.company.auth.UserService;
import com.company.business.models.food.FoodType;
import com.company.business.repositories.food.FoodTypeRepository;
import com.company.business.repositories.people.PeopleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

import static com.company.auth.UserService.CheckUserResult.NO_USER;
import static com.company.auth.UserService.CheckUserResult.SUCCESS;

@Controller
@RequestMapping("/")
public class CommonController {
  private final static Logger logger = LoggerFactory.getLogger(CommonController.class);
  private final SessionRepository sessionRepository;
  private final UserService userService;
  private final FoodTypeRepository foodTypeRepository;
  private final PeopleRepository peopleRepository;

  public CommonController(SessionRepository sessionRepository, UserService userService, FoodTypeRepository foodTypeRepository, PeopleRepository peopleRepository) {
    this.sessionRepository = sessionRepository;
    this.userService = userService;
    this.foodTypeRepository = foodTypeRepository;
    this.peopleRepository = peopleRepository;
  }

  @GetMapping("/")
  public String index(Model model) {
    var session = sessionRepository.get(getSessionId());
    if (session == null)
      return "loginPage";

    var user = (User) model.getAttribute("user");
    var person = peopleRepository.getById(user.getPersonId());
    model.addAttribute("person", person);
    return "index";
  }

  @PostMapping("sign-in")
  public String sigIn(Model model) {
    var login = (String) model.getAttribute("login");
    var password = (String) model.getAttribute("password");
    if (userService.check(new User(login, password, null)) != SUCCESS) {
      logger.error("Can't sign in with login = '" + login + "'");
      model.addAttribute("error", true);
      return "loginPage";
    }

    sessionRepository.store(new Session(getSessionId(), userService.get(login)));

    return "index";
  }

  @PostMapping("sign-up")
  public String signUp(Model model) {
    var login = (String) model.getAttribute("login");
    var password = (String) model.getAttribute("password");

    var newUser = new User(login, password, null);
    var checkUserResult = userService.check(newUser);
    if (checkUserResult != NO_USER) {
      logger.error("User '" + login + "' is already exists");
      model.addAttribute("error", true);
      return "signupPage";
    }

    var personName = (String) model.getAttribute("name");
    var person = peopleRepository.getByName(personName);
    newUser.setPersonId(person.getId());
    userService.save(newUser);
    sessionRepository.store(new Session(getSessionId(), newUser));

    model.addAttribute("person", person);

    return "index";
  }

  @PostMapping
  public String logout() {
    sessionRepository.delete(getSessionId());
    return "redirect:/";
  }

  @GetMapping(path = "menu")
  public List<FoodType> menu() {
    return foodTypeRepository.getAll();
  }

  private static String getSessionId() {
    return RequestContextHolder.currentRequestAttributes().getSessionId();
  }
}
