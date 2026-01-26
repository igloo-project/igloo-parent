package org.igloo.jpa.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.Arrays;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Allow to retrieve {@link EntityManager} or {@link EntityTransaction} as test parameters. {@link
 * EntityManagerFactory} is retrieved from spring context. This extension needs to be used in
 * spring-enabled tests.
 */
public class SpringEntityManagerExtension implements ParameterResolver {

  public SpringEntityManagerExtension() {
    // nothing
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    boolean isSupportedType =
        Arrays.asList(EntityManager.class, EntityTransaction.class)
            .contains(parameterContext.getParameter().getType());
    boolean isJdbcDriverAnnotation =
        parameterContext.getParameter().getAnnotation(JdbcDriver.class) != null;
    return isSupportedType || isJdbcDriverAnnotation;
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    EntityManagerFactory entityManagerFactory =
        SpringExtension.getApplicationContext(extensionContext).getBean(EntityManagerFactory.class);
    EntityManagerFactoryWrapper wrapper =
        extensionContext
            .getStore(Namespace.GLOBAL)
            .computeIfAbsent(
                "entityManagerFactoryWrapper",
                p -> new EntityManagerFactoryWrapper(entityManagerFactory),
                EntityManagerFactoryWrapper.class);
    if (parameterContext.getParameter().getType().equals(EntityManagerFactory.class)) {
      return wrapper.emf;
    } else if (parameterContext.getParameter().getType().equals(EntityManager.class)) {
      wrapper.em = wrapper.emf.createEntityManager();
      return wrapper.em;
    } else if (parameterContext.getParameter().getType().equals(EntityTransaction.class)) {
      wrapper.t = wrapper.em.getTransaction();
      wrapper.t.begin();
      return wrapper.t;
    } else if (parameterContext.getParameter().getAnnotation(JdbcDriver.class) != null) {
      String driverClassName =
          (String) wrapper.emf.getProperties().get("jakarta.persistence.jdbc.driver");
      try {
        return Class.forName(driverClassName);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(String.format("%s driver not found", driverClassName), e);
      }
    } else {
      throw new IllegalStateException("Unkown parameter " + parameterContext.toString());
    }
  }

  static class EntityManagerFactoryWrapper implements AutoCloseable {
    final EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction t;

    public EntityManagerFactoryWrapper(EntityManagerFactory emf) {
      this.emf = emf;
    }

    @Override
    public void close() {
      if (t != null && t.isActive()) {
        t.rollback();
      }
      if (em != null && em.isOpen()) {
        em.close();
      }
      // this extension DOES NOT manage entityManagerFactory lifecycle
    }
  }
}
