package com.company.business.repositories.people;

import com.company.business.models.people.Worker;

import java.util.List;

public interface WorkerRepository {
  List<Worker> getAll();

  Worker getById(int id);

  void save(Worker worker);

  void updateProfession(Worker worker);
}
