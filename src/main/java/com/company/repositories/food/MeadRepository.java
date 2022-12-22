package com.company.repositories.food;

import com.company.models.food.Mead;

import java.util.List;

public interface MeadRepository {
  List<Mead> getAll();

  Mead getByName(String name);

  int save(Mead foodType);
}
