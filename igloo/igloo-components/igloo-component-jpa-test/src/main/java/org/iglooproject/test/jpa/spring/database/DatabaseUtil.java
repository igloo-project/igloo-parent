package org.iglooproject.test.jpa.spring.database;

import com.google.common.base.Stopwatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.PatternMatchUtils;

public class DatabaseUtil {

  static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
  static final String TABLE_QUERY =
      """
SELECT
    table_schema || '.' || table_name
FROM
    information_schema.tables
WHERE
    table_type = 'BASE TABLE'
AND
    table_schema NOT IN ('pg_catalog', 'information_schema')
ORDER BY table_schema, table_name
""";

  private DatabaseUtil() {}

  public static void cleanDatabase(
      TransactionTemplate transactionTemplate,
      JdbcTemplate jdbcTemplate,
      DatabaseCleanerProperties cleanerProperties) {
    Stopwatch sw = Stopwatch.createStarted();
    // fetch all table names ; filter out excludes and add extra tables
    List<String> tables = new ArrayList<>();
    List<String> allTables = queryTables(jdbcTemplate);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "Database - All tables: {}", allTables.stream().collect(Collectors.joining(", ")));
    }
    tables.addAll(
        allTables.stream()
            .filter(t -> DatabaseUtil.filterOutExcludeTables(cleanerProperties.getExcludes(), t))
            .toList());
    if (cleanerProperties.getTables().length > 0) {
      tables.addAll(Arrays.stream(cleanerProperties.getTables()).toList());
    }
    // perform select count(*) on all tables
    Integer[] tableCounts =
        tables.stream()
            .<String>map("select count(*) from %s;"::formatted)
            .<Integer>map(q -> jdbcTemplate.queryForObject(q, Integer.class))
            .<Integer>toArray(i -> new Integer[i]);
    // list not-empty tables
    List<String> tablesToTruncate =
        IntStream.range(0, tables.size())
            .filter(i -> tableCounts[i] > 0)
            .mapToObj(tables::get)
            .toList();
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Database - Not empty tables: {}", String.join(", ", tablesToTruncate));
    }
    // perform truncate on all not-empty tables
    String[] truncateQuery =
        tablesToTruncate.stream()
            .<String>map("truncate table %s cascade;"::formatted)
            .toArray(i -> new String[i]);
    LOGGER.info("Database - Truncate prepared: {} ms.", sw.elapsed(TimeUnit.MILLISECONDS));
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "Database - Truncate query:\n{}",
          Stream.of(truncateQuery).collect(Collectors.joining("\n")));
    }
    // perform truncates
    if (truncateQuery.length > 0) {
      transactionTemplate.execute(t -> jdbcTemplate.batchUpdate(truncateQuery));
    }
    LOGGER.info("Database - Duration: {} ms.", sw.elapsed(TimeUnit.MILLISECONDS));
  }

  private static List<String> queryTables(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForList(DatabaseUtil.TABLE_QUERY, String.class);
  }

  static boolean filterOutExcludeTables(String[] excludes, String table) {
    return PatternMatchUtils.simpleMatch(excludes, table);
  }
}
