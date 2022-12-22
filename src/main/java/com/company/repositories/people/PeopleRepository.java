package com.company.repositories.people;

import com.company.models.people.Person;

import java.util.List;

public interface PeopleRepository {
  List<Person> getAll();
  Person getByName(String name);
}
