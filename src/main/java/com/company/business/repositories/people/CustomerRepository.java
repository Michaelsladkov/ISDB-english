package com.company.business.repositories.people;

import com.company.business.models.people.Customer;

import java.util.List;

public interface CustomerRepository {
  List<Customer> getAll();

  Customer get(int id);

  void save(Customer customer);
}
