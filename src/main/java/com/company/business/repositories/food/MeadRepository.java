package com.company.business.repositories.food;

import com.company.business.models.food.Mead;

import java.util.List;

public interface MeadRepository {
  List<Mead> getAll();

  int save(Mead foodType);
}
