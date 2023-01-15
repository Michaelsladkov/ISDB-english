package com.company.business.repositories.people;

import com.company.business.models.people.Person;

import java.util.List;

public interface PeopleRepository {
  List<Person> getAll();
  Person getById(int id);
  Person getByName(String name);
  Integer save(Person person);
}
