package com.company.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcSessionRepository implements SessionRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcSessionRepository.class);
  private final UserRepository userRepository;
  private final JdbcTemplate jdbcTemplate;

  private final static RowMapper<DbSession> sessionRowMapper = (rs, rowNum) -> {
    String sessionId = rs.getString("session_id");
    ;
    String userLogin = rs.getString("user_login");
    return new DbSession(sessionId, userLogin);
  };

  public JdbcSessionRepository(UserRepository userRepository, JdbcTemplate jdbcTemplate) {
    this.userRepository = userRepository;
    this.jdbcTemplate = jdbcTemplate;
  }


  @Override
  public Session get(String sessionId) {
    List<DbSession> dbSessionByList = jdbcTemplate.query(GET_QUERY, sessionRowMapper, sessionId);
    if(dbSessionByList.isEmpty()) {
      logger.error("Can't find session with id = " + sessionId);
      return null;
    }
    var dbSession = dbSessionByList.get(0);
    var user = userRepository.get(dbSession.userLogin);
    if(user == null) return null;

    return new Session(sessionId, user);
  }

  @Override
  public void store(Session session) {
    jdbcTemplate.update(INSERT_QUERY, session.getSessionId(), session.getUser().getLogin());
  }

  @Override
  public void delete(String sessionId) {
    jdbcTemplate.update(DELETE_QUERY, sessionId);
  }

  private static class DbSession {
    final String sessionId;
    final String userLogin;

    private DbSession(String sessionId, String userLogin) {
      this.sessionId = sessionId;
      this.userLogin = userLogin;
    }
  }

  private final static String GET_QUERY =
    "select session_id, user_login from sessions where session_id = ?";

  private final static String INSERT_QUERY =
    "insert into sessions (session_id, user_login) values (?, ?)";

  private final static String DELETE_QUERY =
    "delete from sessions where session_id = ?";
}
