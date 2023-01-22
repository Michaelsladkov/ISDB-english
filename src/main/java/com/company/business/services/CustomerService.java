package com.company.business.services;

import com.company.business.models.people.Customer;
import com.company.business.repositories.people.BanRepository;
import com.company.business.repositories.people.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
  private final CustomerRepository repository;
  private final BanRepository banRepository;

  public CustomerService(CustomerRepository repository, BanRepository banRepository) {
    this.repository = repository;
    this.banRepository = banRepository;
  }

  public Customer get(int id) {
    return repository.getById(id);
  }

  public boolean checkForBan(Customer customer) {
    return banRepository.getByCustomerId(customer.getId()) != null;
  }
}
