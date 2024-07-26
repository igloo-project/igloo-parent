package igloo.hibernateconfig.api;

import java.util.HashSet;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

/** DO NOT IMPORT hibernate-search code so that class can be loaded without hibernate search. */
public interface HibernateSearchConfigurator {

  HibernateSearchBackend getBackend();

  void configureHibernateSearch(IJpaPropertiesProvider configuration, Properties properties);

  static HibernateSearchConfigurator load(HibernateSearchBackend backend) {
    Set<HibernateSearchConfigurator> found = new HashSet<>();
    Set<HibernateSearchConfigurator> all = new HashSet<>();
    ServiceLoader.load(HibernateSearchConfigurator.class)
        .forEach(
            i -> {
              all.add(i);
              if (backend.equals(i.getBackend())) {
                found.add(i);
              }
            });
    if (found.isEmpty()) {
      throw new RuntimeException(
          String.format(
              "No %s (backend=%s) found in [%s]; check your classpath.",
              HibernateSearchConfigurator.class.getName(),
              backend,
              all.stream().map(Object::toString).collect(Collectors.joining(", "))));
    } else {
      return found.iterator().next();
    }
  }
}
