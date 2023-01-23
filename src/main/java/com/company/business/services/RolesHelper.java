package com.company.business.services;

import com.company.business.models.people.Person;
import com.company.business.models.people.Role;
import com.company.business.repositories.people.CustomerRepository;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.repositories.people.WorkerRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.company.business.models.people.Role.*;

@Component
public class RolesHelper {
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
      case MANAGER -> resultRoles.add(ADMIN);
    }

    return resultRoles;
  }
}
