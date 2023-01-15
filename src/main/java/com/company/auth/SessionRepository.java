package com.company.auth;

public interface SessionRepository {
  Session get(String id);

  void store(Session session);

  void delete(String id);
}
