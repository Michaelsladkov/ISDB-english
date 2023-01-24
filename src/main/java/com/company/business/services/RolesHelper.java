package com.company.business.services;

import com.company.business.models.people.Person;
import com.company.business.models.people.Role;
import com.company.business.repositories.people.CustomerRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.repositories.people.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.company.business.models.people.Role.*;

@Component
public class RolesHelper {
  private static final Logger logger = LoggerFactory.getLogger(RolesHelper.class);
  private final PeopleRepository peopleRepository;
  private final WorkerRepository workerRepository;
  private final CustomerRepository customerRepository;

  public RolesHelper(PeopleRepository peopleRepository, WorkerRepository workerRepository, CustomerRepository customerRepository) {
    this.peopleRepository = peopleRepository;
    this.workerRepository = workerRepository;
    this.customerRepository = customerRepository;
  }

  public Set<Role> getRoles(Person person) {
    var resultRoles = new HashSet<Role>();
    var customer = customerRepository.get(person.getId());
    if (customer != null)
      resultRoles.add(CUSTOMER);
    var worker = workerRepository.getById(person.getId());
    if (worker == null)
      return resultRoles;

    switch (worker.getProfession()) {
      case STOREKEEPER -> resultRoles.add(STORAGE_MANAGER);
      case WAITER -> resultRoles.add(ORDERS_MANAGER);
      case MANAGER -> resultRoles.addAll(List.of(ADMIN, ORDERS_MANAGER, STORAGE_MANAGER));
      case OWNER -> resultRoles.addAll(List.of(OWNER, ADMIN, ORDERS_MANAGER, STORAGE_MANAGER));
    }

    return resultRoles;
  }

  public boolean validateRole(Person person, Role targetRole) {
    if (!getRoles(person).contains(targetRole)) {
      logger.error("Person with id = '" + person.getId() + "' hasn't got permissions for '" + targetRole + "' role");
      return false;
    }

    return true;
  }
}
