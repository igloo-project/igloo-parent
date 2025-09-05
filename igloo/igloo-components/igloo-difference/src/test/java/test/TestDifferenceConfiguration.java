package test;

import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import java.util.Locale;
import org.iglooproject.commons.util.context.ExecutionContexts;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.service.IHistoryValueService;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import test.business.dao.ITestHistoryLogDao;
import test.business.dao.TestHistoryLogDaoImpl;
import test.business.model.TestHistoryLog;
import test.business.model.TestUser;
import test.business.service.ISubjectService;
import test.business.service.ITestHistoryLogService;
import test.business.service.TestHistoryLogServiceImpl;
import test.business.service.TestHistoryValueServiceImpl;

@Configuration
@Import(PsqlTestContainerConfiguration.class)
@EntityScan(basePackageClasses = {TestHistoryLog.class, AbstractHistoryLog.class})
public class TestDifferenceConfiguration {
  @Bean
  public ISubjectService subjectService() {
    return () -> new TestUser(1l);
  }

  @Bean
  public ITestHistoryLogDao historyLogDao() {
    return new TestHistoryLogDaoImpl();
  }

  @Bean
  public ITestHistoryLogService historyLogService(
      ITestHistoryLogDao dao, ISubjectService subjectService) {
    return new TestHistoryLogServiceImpl(dao, subjectService);
  }

  @Bean
  public IHistoryValueService historyValueService(ITestHistoryLogDao dao) {
    return new TestHistoryValueServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public IRendererService rendererService() {
    @SuppressWarnings("rawtypes")
    IRenderer renderer =
        new IRenderer() {
          @Override
          public String render(Object value, Locale locale) {
            if (value == null) {
              return "null";
            } else {
              return value.toString();
            }
          }
        };
    return new IRendererService() {

      @Override
      public IExecutionContext context() {
        return ExecutionContexts.noOp();
      }

      @Override
      public IExecutionContext context(Locale locale) {
        return ExecutionContexts.noOp();
      }

      @SuppressWarnings("unchecked")
      @Override
      public <T> IRenderer<? super T> findRenderer(Class<T> valueType) {
        return renderer;
      }

      @SuppressWarnings("unchecked")
      @Override
      public <T> IRenderer<? super T> findRenderer(Class<?> rootType, Class<T> valueType) {
        return renderer;
      }

      @SuppressWarnings("unchecked")
      @Override
      public <T> IRenderer<? super T> findRenderer(
          Class<?> rootType, FieldPath path, Class<T> valueType) {
        return renderer;
      }

      @Override
      public String localize(String resourceKey) {
        return resourceKey;
      }

      @Override
      public String localize(
          String resourceKey, Object namedParameters, Object... positionalParameters) {
        return resourceKey;
      }

      @Override
      public String localize(Enum<?> enumValue, String prefix, String suffix) {
        return enumValue != null ? enumValue.name() : "null";
      }

      @Override
      public <T> String localize(Class<T> clazz, T value) {
        if (value == null) {
          return null;
        } else {
          return value.toString();
        }
      }

      @Override
      public String localize(String resourceKey, Locale locale) {
        return resourceKey;
      }

      @Override
      public String localize(
          String resourceKey,
          Locale locale,
          Object namedParameters,
          Object... positionalParameters) {
        return resourceKey;
      }

      @Override
      public String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale) {
        return localize(enumValue, prefix, suffix);
      }

      @Override
      public <T> String localize(Class<T> clazz, T value, Locale locale) {
        return localize(clazz, value);
      }
    };
  }
}
