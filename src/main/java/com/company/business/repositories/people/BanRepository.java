package com.company.business.repositories.people;

import com.company.business.models.people.Ban;

public interface BanRepository {
  Ban get(int id);

  Ban getByCustomerId(int customerId);

  Integer add(Ban ban);

  void delete(int customerId);
}
