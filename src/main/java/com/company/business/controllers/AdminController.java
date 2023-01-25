package com.company.business.controllers;

import com.company.auth.SessionRepository;
import com.company.business.models.food.Mead;
import com.company.business.models.people.Ban;
import com.company.business.repositories.food.MeadRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.services.CustomerService;
import com.company.business.services.OwnerService;
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

import java.time.LocalDate;
import java.util.Map;

import static com.company.business.models.people.Role.ADMIN;
import static com.company.business.models.people.Role.OWNER;
import static com.company.business.models.people.Worker.Profession;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {
  private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
  private final RolesHelper rolesHelper;
  private final WorkerService workerService;
  private final CustomerService customerService;
  private final OwnerService ownerService;
  private final MeadRepository meadRepository;

  public AdminController(SessionRepository sessionRepository, PeopleRepository peopleRepository, RolesHelper rolesHelper, WorkerService workerService, CustomerService customerService, OwnerService ownerService, MeadRepository meadRepository) {
    super(sessionRepository, peopleRepository);
    this.rolesHelper = rolesHelper;
    this.workerService = workerService;
    this.customerService = customerService;
    this.ownerService = ownerService;
    this.meadRepository = meadRepository;
  }

  @GetMapping({"/", "index", ""})
  String index(Model model) {
    if (!rolesHelper.validateRole(getPerson(), ADMIN))
      return "redirect:/";

    var workers = workerService.getAll();
    model.addAllAttributes(Map.of(
      "workers", workers
    ));

    return "adminPage";
  }

  @PostMapping("worker")
  String addWorker(HttpServletRequest request, Model model) {
    if (!rolesHelper.validateRole(getPerson(), ADMIN))
      return "redirect:/";

    var name = request.getParameter("name");
    var profession = Profession.valueOf(request.getParameter("profession"));

    workerService.save(name, profession);
    return "redirect:/admin/";
  }

  @PostMapping("ban")
  String addBan(HttpServletRequest request, Model model) {
    if (!rolesHelper.validateRole(getPerson(), ADMIN))
      return "redirect:/";

    var customerName = request.getParameter("name");
    var days = Integer.parseInt(request.getParameter("days"));

    var from = LocalDate.now();
    var to = from.plusDays(days);
    var customer = customerService.get(customerName);
    var newBan = new Ban(null, customer.getId(), from, to);

    customerService.addBan(newBan);

    return "redirect:/admin/";
  }

  @PostMapping("ban/remove")
  String removeBan(HttpServletRequest request, Model model) {
    if (!rolesHelper.validateRole(getPerson(), ADMIN))
      return "redirect:/";

    var customerName = request.getParameter("name");
    customerService.removeBan(customerService.get(customerName));

    return "redirect:/admin/";
  }

  @PostMapping("/owner/red-prohibited-button")
  public String redProhibitedButton() {
    if (!rolesHelper.validateRole(getPerson(), OWNER))
      return "redirect:/";
    ownerService.destroy();
    return "blackhole";
  }

  @PostMapping("/owner/mead")
  public String redProhibitedButton(HttpServletRequest request, Model model) {
    if (!rolesHelper.validateRole(getPerson(), OWNER))
      return "redirect:/";

    String foodTypeName = request.getParameter("name");
    int price = Integer.parseInt(request.getParameter("price"));
    int hp = Integer.parseInt(request.getParameter("hp"));
    int mana = Integer.parseInt(request.getParameter("mana"));
    int stamina = Integer.parseInt(request.getParameter("stamina"));

    int alcohol = Integer.parseInt(request.getParameter("alcohol"));

    var mead = new Mead(null, foodTypeName, price, hp, mana, stamina, alcohol);

    meadRepository.save(mead);

    return "redirect:/worker/storage";
  }
}
