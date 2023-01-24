package com.company.business.repositories.people;

import com.company.business.models.people.LoyaltyLevel;

import java.util.List;

public interface LoyaltyLevelRepository {
  List<LoyaltyLevel> get();

  LoyaltyLevel get(int level);

  void save(LoyaltyLevel level);
}
