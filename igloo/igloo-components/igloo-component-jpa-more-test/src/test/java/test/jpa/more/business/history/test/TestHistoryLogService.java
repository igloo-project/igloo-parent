package test.jpa.more.business.history.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import org.iglooproject.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.more.test.junit.difference.TestHistoryDifferenceCollectionMatcher;
import org.iglooproject.jpa.more.test.junit.difference.TestHistoryDifferenceDescription;
import org.iglooproject.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import test.jpa.more.business.AbstractJpaMoreTestCase;
import test.jpa.more.business.entity.model.TestEntity;
import test.jpa.more.business.history.model.TestHistoryDifference;
import test.jpa.more.business.history.model.TestHistoryLog;
import test.jpa.more.business.history.model.atomic.TestHistoryEventType;
import test.jpa.more.business.history.model.bean.TestHistoryLogAdditionalInformationBean;
import test.jpa.more.business.history.service.ITestHistoryLogService;
import test.jpa.more.config.spring.SpringBootTestJpaMoreHistoryLog;

@SpringBootTestJpaMoreHistoryLog
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestHistoryLogService extends AbstractJpaMoreTestCase {

  // database precision is micros
  private static final Instant DATE = Instant.now().truncatedTo(ChronoUnit.MICROS);

  /*
   * Only here to mock some parameters passed to the log() method.
   * The Spring context is still used for most beans.
   */

  @Mock(answer = Answers.RETURNS_MOCKS)
  private IDifferenceFromReferenceGenerator<TestEntity> differenceGeneratorMock;

  @Mock private IHistoryDifferenceGenerator<TestEntity> historyDifferenceGeneratorMock;

  @Autowired private ITestHistoryLogService historyLogService;

  @Autowired private IEntityService entityService;

  @Autowired
  private ITransactionSynchronizationTaskManagerService transactionSynchronizationService;

  private TransactionTemplate writeTransactionTemplate;

  private HistoryValue entityHistoryValueBefore;
  private HistoryValue entityHistoryValueAfter;

  private HistoryValue stringHistoryValueAfter = new HistoryValue("after");

  private HistoryValue createExpectedHistoryValue(TestEntity entity) {
    return new HistoryValue(entity.toString(), GenericEntityReference.of(entity));
  }

  @Autowired
  private void setTransactionTemplate(PlatformTransactionManager transactionManager) {
    DefaultTransactionAttribute writeTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
    writeTransactionAttribute.setReadOnly(false);
    writeTransactionTemplate =
        new TransactionTemplate(transactionManager, writeTransactionAttribute);
  }

  @BeforeEach
  public void initValues() throws ServiceException, SecurityServiceException {
    TestEntity before = new TestEntity("beforeEntity");
    TestEntity after = new TestEntity("afterEntity");
    testEntityService.create(before);
    testEntityService.create(after);
    entityHistoryValueBefore = createExpectedHistoryValue(before);
    entityHistoryValueAfter = createExpectedHistoryValue(after);
  }

  @BeforeEach
  public void initMocks() {
    // Make the difference generation fail if the modified object is not attached to the session
    AssertionError error =
        new AssertionError(
            "Attempt to compute differences on an object that was not attached to the session");
    when(differenceGeneratorMock.diff(
            MockitoHamcrest.argThat(not(this.<TestEntity>isAttachedToSession())),
            ArgumentMatchers.<TestEntity>any()))
        .thenThrow(error);
    when(differenceGeneratorMock.diffFromReference(
            MockitoHamcrest.argThat(not(this.<TestEntity>isAttachedToSession()))))
        .thenThrow(error);
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    cleanEntities(historyLogService);
    super.cleanAll();
  }

  private List<TestHistoryDifference> createExpectedDifferences() {
    TestHistoryDifference difference1 =
        new TestHistoryDifference(
            new HistoryDifferencePath(FieldPath.fromString(".somePropertyIJustInvented")),
            HistoryDifferenceEventType.ADDED,
            entityHistoryValueBefore,
            entityHistoryValueAfter);
    TestHistoryDifference difference2 =
        new TestHistoryDifference(
            new HistoryDifferencePath(
                FieldPath.fromString(".somePropertyIJustInvented2").item(), new HistoryValue("1")),
            HistoryDifferenceEventType.REMOVED,
            null,
            stringHistoryValueAfter);
    return Lists.newArrayList(difference1, difference2);
  }

  private Matcher<Collection<TestHistoryDifference>> matchesExpectedDifferences() {
    return new TestHistoryDifferenceCollectionMatcher<>(
        TestHistoryDifferenceDescription.builder()
            .put(
                FieldPath.fromString(".somePropertyIJustInvented"),
                HistoryDifferenceEventType.ADDED)
            .putItem(
                FieldPath.fromString(".somePropertyIJustInvented2"),
                1,
                HistoryDifferenceEventType.REMOVED)
            .build());
  }

  @Test
  void logNow() throws ServiceException, SecurityServiceException {
    TestEntity object = new TestEntity("object");
    testEntityService.create(object);

    TestEntity secondaryObject = new TestEntity("secondaryObject");
    testEntityService.create(secondaryObject);

    List<TestHistoryDifference> differences = createExpectedDifferences();

    historyLogService.logNow(
        DATE,
        TestHistoryEventType.EVENT1,
        differences,
        object,
        TestHistoryLogAdditionalInformationBean.of(secondaryObject));

    HistoryValue expectedObjectHistoryValue = createExpectedHistoryValue(object);
    HistoryValue expectedSecondaryObjectHistoryValue = createExpectedHistoryValue(secondaryObject);

    entityService.flush();
    entityService.clear();

    List<TestHistoryLog> logs = historyLogService.list();

    assertThat(logs).hasSize(1);

    TestHistoryLog log = logs.iterator().next();

    assertThat(log.getId()).isNotNull();

    assertThat(log.getDate()).isEqualTo(DATE);
    assertThat(log.getEventType()).isEqualTo(TestHistoryEventType.EVENT1);
    assertThat(log.getMainObject()).isEqualTo(expectedObjectHistoryValue);
    assertThat(log.getObject1()).isEqualTo(expectedSecondaryObjectHistoryValue);
    assertThat(log.getDifferences(), matchesExpectedDifferences());
  }

  @Test
  void logBeforeCommit() throws ServiceException, SecurityServiceException {
    final TestEntity object = new TestEntity("object");
    testEntityService.create(object);

    final TestEntity secondaryObject = new TestEntity("secondaryObject");
    testEntityService.create(secondaryObject);

    HistoryValue expectedObjectHistoryValue = createExpectedHistoryValue(object);
    HistoryValue expectedSecondaryObjectHistoryValue = createExpectedHistoryValue(secondaryObject);

    entityService.flush();
    entityService.clear();

    Mockito.when(
            historyDifferenceGeneratorMock.toHistoryDifferences(
                ArgumentMatchers.<Supplier2<TestHistoryDifference>>any(),
                ArgumentMatchers.<Difference<TestEntity>>any()))
        .then(
            new Answer<List<TestHistoryDifference>>() {
              @Override
              public List<TestHistoryDifference> answer(InvocationOnMock invocation)
                  throws Throwable {
                return createExpectedDifferences();
              }
            });

    final Instant before = Instant.now();

    writeTransactionTemplate.execute(
        new TransactionCallbackWithoutResult() {
          @SuppressWarnings("unchecked")
          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            TestEntity objectReloaded = entityService.getEntity(object);
            TestEntity secondaryObjectReloaded = entityService.getEntity(secondaryObject);
            try {
              historyLogService.logWithDifferences(
                  TestHistoryEventType.EVENT1,
                  objectReloaded,
                  TestHistoryLogAdditionalInformationBean.of(secondaryObjectReloaded),
                  differenceGeneratorMock,
                  historyDifferenceGeneratorMock);
            } catch (ServiceException | SecurityServiceException e) {
              throw new IllegalStateException(e);
            }
          }
        });

    final Instant after = Instant.now();

    List<TestHistoryLog> logs = historyLogService.list();

    assertThat(logs).hasSize(1);

    TestHistoryLog log = logs.iterator().next();

    assertThat(log.getId()).isNotNull();

    assertThat(
        log.getDate(),
        new TypeSafeMatcher<Instant>() {
          @Override
          public void describeTo(Description description) {
            description
                .appendText("a date between ")
                .appendValue(before)
                .appendText(" and ")
                .appendValue(after);
          }

          @Override
          protected boolean matchesSafely(Instant item) {
            return !item.isBefore(before) && !item.isAfter(after);
          }
        });
    assertThat(log.getEventType()).isEqualTo(TestHistoryEventType.EVENT1);
    assertThat(log.getMainObject()).isEqualTo(expectedObjectHistoryValue);
    assertThat(log.getObject1()).isEqualTo(expectedSecondaryObjectHistoryValue);
    assertThat(log.getDifferences(), matchesExpectedDifferences());
  }

  @Test
  void logBeforeClear() throws ServiceException, SecurityServiceException {
    final TestEntity object = new TestEntity("object");
    testEntityService.create(object);

    final TestEntity secondaryObject = new TestEntity("secondaryObject");
    testEntityService.create(secondaryObject);

    HistoryValue expectedObjectHistoryValue = createExpectedHistoryValue(object);
    HistoryValue expectedSecondaryObjectHistoryValue = createExpectedHistoryValue(secondaryObject);

    entityService.flush();
    entityService.clear();

    Mockito.when(
            historyDifferenceGeneratorMock.toHistoryDifferences(
                ArgumentMatchers.<Supplier2<TestHistoryDifference>>any(),
                ArgumentMatchers.<Difference<TestEntity>>any()))
        .then(
            new Answer<List<TestHistoryDifference>>() {
              @Override
              public List<TestHistoryDifference> answer(InvocationOnMock invocation)
                  throws Throwable {
                return createExpectedDifferences();
              }
            });

    final Instant before = Instant.now();

    writeTransactionTemplate.execute(
        new TransactionCallbackWithoutResult() {
          @SuppressWarnings("unchecked")
          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            TestEntity objectReloaded = entityService.getEntity(object);
            TestEntity secondaryObjectReloaded = entityService.getEntity(secondaryObject);
            try {
              historyLogService.logWithDifferences(
                  TestHistoryEventType.EVENT1,
                  objectReloaded,
                  TestHistoryLogAdditionalInformationBean.of(secondaryObjectReloaded),
                  differenceGeneratorMock,
                  historyDifferenceGeneratorMock);

              // Simulate a batch treatment, that flushes and clears the session repeatedly
              entityService.flush();
              transactionSynchronizationService.beforeClear();
              entityService.clear();

            } catch (ServiceException | SecurityServiceException e) {
              throw new IllegalStateException(e);
            }
          }
        });

    final Instant after = Instant.now();

    List<TestHistoryLog> logs = historyLogService.list();

    assertThat(logs).hasSize(1);

    TestHistoryLog log = logs.iterator().next();

    assertThat(log.getId()).isNotNull();

    assertThat(
        log.getDate(),
        new TypeSafeMatcher<Instant>() {
          @Override
          public void describeTo(Description description) {
            description
                .appendText("a date between ")
                .appendValue(before)
                .appendText(" and ")
                .appendValue(after);
          }

          @Override
          protected boolean matchesSafely(Instant item) {
            return !item.isBefore(before) && !item.isAfter(after);
          }
        });
    assertThat(log.getEventType()).isEqualTo(TestHistoryEventType.EVENT1);
    assertThat(log.getMainObject()).isEqualTo(expectedObjectHistoryValue);
    assertThat(log.getObject1()).isEqualTo(expectedSecondaryObjectHistoryValue);
    assertThat(log.getDifferences(), matchesExpectedDifferences());
  }
}
