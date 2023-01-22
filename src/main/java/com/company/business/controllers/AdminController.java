package com.company.business.controllers;

import com.company.auth.SessionRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.RolesHelper;
import com.company.business.services.WorkerService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.company.business.models.people.Role.ADMIN;
import static com.company.business.models.people.Role.OWNER;
import static com.company.business.models.people.Worker.Profession;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {
  private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
  private final RolesHelper rolesHelper;
  private final WorkerService workerService;

  public AdminController(SessionRepository sessionRepository, PeopleRepository peopleRepository, RolesHelper rolesHelper, WorkerService workerService) {
    super(sessionRepository, peopleRepository);
    this.rolesHelper = rolesHelper;
    this.workerService = workerService;
  }

  @GetMapping({"/", "index"})
  String index(Model model) {
    if (!validateRole())
      return "redirect:/";
    return "adminPage";
  }

  @PostMapping("worker")
  String addWorker(HttpServletRequest request, Model model) {
    if (!validateRole())
      return "redirect:/";

    var name = request.getParameter("name");
    var profession = Profession.valueOf(request.getParameter("profession"));

    workerService.save(name, profession);
    return "redirect:/admin/";
  }

  private boolean validateRole() {
    var person = getPerson();
    var roles = rolesHelper.getRoles(person);
    if (!(roles.contains(ADMIN) || roles.contains(OWNER))) {
      logger.error("Person with id = '" + person.getId() + "' hasn't got permissions for manage workers");
      return false;
    }

    return true;
  }
}
