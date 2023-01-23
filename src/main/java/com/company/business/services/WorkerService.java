package com.company.business.services;

import com.company.business.models.people.Worker;
import com.company.business.repositories.people.PeopleRepository;
import com.company.business.repositories.people.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.company.business.models.people.Worker.Profession;

@Service
public class WorkerService {
  private final WorkerRepository repository;
  private final PeopleRepository peopleRepository;

  public WorkerService(WorkerRepository repository, PeopleRepository peopleRepository) {
    this.repository = repository;
    this.peopleRepository = peopleRepository;
  }

  public List<Worker> getAll() {
    return repository.getAll();
  }

  public void save(String name, Profession profession) {
    var person = peopleRepository.getByName(name);
    var worker = new Worker(
      person.getId(), person.getName(), person.getBirthday(),
      person.getHp(), person.getMana(), person.getStamina(),
      profession
    );

    repository.save(worker);
  }
}
