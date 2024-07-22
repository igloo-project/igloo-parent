package org.iglooproject.jpa.migration.service;

import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MigrationUtilsServiceImpl implements IMigrationUtilsService {

  public static final String SQL_UPDATE_SEQUENCE =
      "SELECT setval('%1$s_id_seq', (SELECT max(id) FROM %1$s))";

  protected JdbcTemplate newDatabaseJdbcTemplate;

  @Override
  public void updateSequence(Class<?> clazz) {
    // TODO SDO : Trouver un moyen de vérifier de manière plus sûre
    if (StringUtils.endsWith(clazz.getSimpleName().toLowerCase(), "user")) {
      // Cas particulier de la table User
      newDatabaseJdbcTemplate.execute(String.format(SQL_UPDATE_SEQUENCE, "user_"));
    } else {
      newDatabaseJdbcTemplate.execute(
          String.format(SQL_UPDATE_SEQUENCE, clazz.getSimpleName().toLowerCase()));
    }
  }

  @Autowired
  public final void setDataSource(@Value("#{dataSource}") DataSource dataSource) {
    newDatabaseJdbcTemplate = new JdbcTemplate(dataSource);
  }
}
