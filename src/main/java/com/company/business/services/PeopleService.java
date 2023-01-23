package com.company.business.services;

import com.company.business.models.people.Person;
import com.company.business.repositories.people.PeopleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PeopleService {
  private static final Logger logger = LoggerFactory.getLogger(PeopleService.class);
  private final PeopleRepository repository;

  public PeopleService(PeopleRepository repository) {
    this.repository = repository;
  }

  public Person get(int id) {
    return repository.getById(id);
  }

  public Person get(String name) {
    return repository.getByName(name);
  }

  public void increaseIndicators(int id, int hp, int mana, int stamina) {
    var person = get(id);
    var newHp = Math.min(person.getHp() + hp, 100);
    var newMana = Math.min(person.getMana() + hp, 100);
    var newStamina = Math.min(person.getStamina() + stamina, 100);
    repository.updateIndicators(id, newHp, newMana, newStamina);
  }
}
