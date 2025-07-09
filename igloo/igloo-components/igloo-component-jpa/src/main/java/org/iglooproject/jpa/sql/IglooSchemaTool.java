package org.iglooproject.jpa.sql;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IglooSchemaTool {
  private static final Logger LOGGER = LoggerFactory.getLogger(IglooSchemaTool.class);

  private final AtomicBoolean freezed = new AtomicBoolean();
  private final Set<SchemaContributor> contributors =
      new TreeSet<>(new SchemaContributorComparator());

  /** Ordered {@link SchemaContributor} list. */
  public Set<SchemaContributor> freezeContributors() {
    freezed.set(true);
    return Collections.unmodifiableSet(contributors);
  }

  public void addContributor(SchemaContributor contributor) {
    if (freezed.get()) {
      throw new IllegalStateException(
          "Contributor %s cannot be added once contributor list is freezed".formatted(contributor));
    }
    if (contributors.contains(contributor)) {
      LOGGER.warn("Trying to add {} twice, contributor add is ignored", contributor);
      return;
    }
    contributors.add(contributor);
  }

  public static class SchemaContributorComparator implements Comparator<SchemaContributor> {
    @Override
    public int compare(SchemaContributor o1, SchemaContributor o2) {
      if (o1 == null && o2 == null) {
        return 0;
      } else if (o1 == null) {
        return 11;
      } else if (o2 == null) {
        return -1;
      } else {
        return Integer.compare(o1.getOrder(), o2.getOrder());
      }
    }
  }
}
