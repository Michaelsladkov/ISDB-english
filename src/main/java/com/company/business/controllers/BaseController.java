package com.company.business.controllers;

import com.company.auth.Session;
import com.company.auth.SessionRepository;
import com.company.auth.User;
import com.company.business.models.people.Person;
import com.company.business.repositories.people.PeopleRepository;
import org.springframework.web.context.request.RequestContextHolder;

abstract public class BaseController {
  protected final SessionRepository sessionRepository;
  protected final PeopleRepository peopleRepository;

  public BaseController(SessionRepository sessionRepository, PeopleRepository peopleRepository) {
    this.sessionRepository = sessionRepository;
    this.peopleRepository = peopleRepository;
  }

  protected User getUser() {
    var session = session();
    if (session == null)
      throw new IllegalStateException("Current session is not bounded to any user");
    return session.getUser();
  }

  protected Person getPerson() {
    var user = getUser();
    return getPerson(user);
  }

  protected Person getPerson(User user) {
    return peopleRepository.getById(user.getPersonId());
  }

  protected Session session() {
    return sessionRepository.get(sessionId());
  }

  protected static String sessionId() {
    return RequestContextHolder.currentRequestAttributes().getSessionId();
  }

}
