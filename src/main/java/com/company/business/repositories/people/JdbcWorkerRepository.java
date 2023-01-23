package com.company.business.repositories.people;

import com.company.business.models.people.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcWorkerRepository implements WorkerRepository {
  private static final Logger logger = LoggerFactory.getLogger(JdbcWorkerRepository.class);
  private final static RowMapper<Worker> workerRowMapper = (rs, rowNum) -> {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    LocalDate birthday = rs.getDate("birthday").toLocalDate();
    int hp = rs.getInt("hp");
    int mana = rs.getInt("mana");
    int stamina = rs.getInt("stamina");
    var profession = Worker.Profession.valueOf(rs.getString("profession"));
    return new Worker(id, name, birthday, hp, mana, stamina, profession);
  };
  private static final String GET_ALL_QUERY =
    "select w.id as id, name, birthday, hp, mana, stamina, profession " +
      "from workers as w inner join people as p using(id)";
  private static final String GET_BY_ID_QUERY =
    "select w.id, id, name, birthday, hp, mana, stamina, profession " +
      "from workers as w inner join people as p using(id) +" +
      "where w.id = ?";
  private static final String INSERT_QUERY =
    "insert into workers (id, profession) values (?, ?)";
  private final JdbcTemplate jdbcTemplate;

  public JdbcWorkerRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Worker> getAll() {
    return jdbcTemplate.query(GET_ALL_QUERY, workerRowMapper);
  }

  @Override
  public Worker getById(int id) {
    List<Worker> workerByList = jdbcTemplate.query(GET_BY_ID_QUERY, workerRowMapper, id);
    if (workerByList.isEmpty()) {
      logger.error("Can't find worker with id = '" + id + "'");
      return null;
    }
    return workerByList.get(0);
  }

  @Override
  public void save(Worker worker) {
    jdbcTemplate.update(INSERT_QUERY, worker.getId(), worker.getProfession().toString());
  }

}
