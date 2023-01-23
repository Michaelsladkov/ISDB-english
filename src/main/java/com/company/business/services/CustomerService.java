package com.company.business.services;

import com.company.business.models.people.Ban;
import com.company.business.models.people.Customer;
import com.company.business.repositories.people.BanRepository;
import com.company.business.repositories.people.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CustomerService {
  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
  private final CustomerRepository repository;
  private final PeopleService peopleService;
  private final BanRepository banRepository;

  public CustomerService(CustomerRepository repository, PeopleService peopleService, BanRepository banRepository) {
    this.repository = repository;
    this.peopleService = peopleService;
    this.banRepository = banRepository;
  }

  public Customer get(int id) {
    return repository.get(id);
  }

  public Customer get(String name) {
    var person = peopleService.get(name);

    return repository.get(person.getId());
  }

  public Integer addBan(Ban ban) {
    return banRepository.add(ban);
  }

  public boolean checkForBan(Customer customer) {
    var ban = banRepository.getByCustomerId(customer.getId());
    var now = LocalDate.now();

    return ban == null
      || !(
      now.compareTo(ban.getFrom()) >= 0 && now.compareTo(ban.getTo()) <= 0
    );
  }
}
