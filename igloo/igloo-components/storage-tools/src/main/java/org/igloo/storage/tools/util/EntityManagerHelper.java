package org.igloo.storage.tools.util;

import jakarta.persistence.EntityManager;
import java.util.function.Function;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.tools.util.action.IDbAction;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

public class EntityManagerHelper {

  private final EntityManagerUtils entityManagerUtils;
  private final DatabaseOperations databaseOperations;
  private final TransactionTemplate readOnlyTransactionTemplate;
  private final TransactionTemplate readWriteTransactionTemplate;

  public EntityManagerHelper(
      EntityManagerUtils entityManagerUtils,
      PlatformTransactionManager platformTransactionManager,
      DatabaseOperations databaseOperations) {
    this.entityManagerUtils = entityManagerUtils;
    this.databaseOperations = databaseOperations;

    {
      DefaultTransactionAttribute readOnlyTransactionAttribute =
          new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
      readOnlyTransactionAttribute.setReadOnly(true);
      readOnlyTransactionTemplate =
          new TransactionTemplate(platformTransactionManager, readOnlyTransactionAttribute);
    }

    {
      DefaultTransactionAttribute readWriteTransactionAttribute =
          new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
      readWriteTransactionAttribute.setReadOnly(false);
      readWriteTransactionTemplate =
          new TransactionTemplate(platformTransactionManager, readWriteTransactionAttribute);
    }
  }

  /** Perform a read-only operation. */
  public <T> T doWithReadOnlyTransaction(Function<EntityManager, T> consumer) {
    return readOnlyTransactionTemplate.execute(
        t -> {
          EntityManager entityManager = entityManagerUtils.getCurrentEntityManager();
          return consumer.apply(entityManager);
        });
  }

  /** Perform a write operation. */
  public <T> T doWithReadWriteTransaction(Function<EntityManager, T> consumer) {
    return readWriteTransactionTemplate.execute(
        t -> {
          EntityManager entityManager = entityManagerUtils.getCurrentEntityManager();
          return consumer.apply(entityManager);
        });
  }

  /** Perform a read-only operation. */
  public <T> T doWithReadOnlyTransaction(IDbAction<T> action) {
    return readOnlyTransactionTemplate.execute(
        t -> {
          EntityManager entityManager = entityManagerUtils.getCurrentEntityManager();
          return action.perform(entityManager, databaseOperations);
        });
  }

  /** Perform a read-only operation. */
  public <T> T doWithReadWriteTransaction(IDbAction<T> action) {
    return readWriteTransactionTemplate.execute(
        t -> {
          EntityManager entityManager = entityManagerUtils.getCurrentEntityManager();
          return action.perform(entityManager, databaseOperations);
        });
  }
}
