package test;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.service.ITransactionScopeIndependantRunnerService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.business.model.HistoryLogAdditionalInformationBean;
import test.business.model.QTestHistoryLog;
import test.business.model.TestData;
import test.business.model.TestHistoryDifference;
import test.business.model.TestHistoryLog;
import test.business.model.TestHistoryLogEventType;
import test.business.model.TestUser;
import test.business.service.ISubjectService;
import test.business.service.ITestDataDifferenceService;
import test.business.service.ITestHistoryLogService;

@SpringBootTestDifference
abstract class AbstractTestHistoryEntityReference {
  @Autowired ITestHistoryLogService historyLogService;

  @Autowired ITestDataDifferenceService testDataDifferenceService;

  @Autowired ISubjectService subjectService;

  @Autowired ITransactionScopeIndependantRunnerService transactionScopeIndependantRunnerService;

  @PersistenceContext private EntityManager entityManager;

  @Test
  void testHistoryEntityReference() throws ServiceException, SecurityServiceException {
    Instant now = Instant.now();

    TestHistoryDifference historyDifference1 =
        new TestHistoryDifference(
            new HistoryDifferencePath(FieldPath.fromString("property1")),
            HistoryDifferenceEventType.UPDATED,
            new HistoryValue("myLabel1", GenericEntityReference.of(TestData.class, 6l)),
            new HistoryValue("myLabel2", GenericEntityReference.of(TestData.class, 7l)));

    TestHistoryDifference historyDifference2 =
        new TestHistoryDifference(
            new HistoryDifferencePath(FieldPath.fromString("property2")),
            HistoryDifferenceEventType.UPDATED,
            new HistoryValue("myLabel1", "\"value1\""),
            new HistoryValue("myLabel2", "\"value2\""));

    TestHistoryDifference historyDifference3 =
        new TestHistoryDifference(
            new HistoryDifferencePath(
                FieldPath.fromString("properties[]"),
                new HistoryValue("myKey", GenericEntityReference.of(TestData.class, 11l))),
            HistoryDifferenceEventType.UPDATED,
            new HistoryValue("myLabel1", "\"value1\""),
            new HistoryValue("myLabel2", "\"value2\""));

    TestHistoryDifference historyDifference4 =
        new TestHistoryDifference(
            new HistoryDifferencePath(FieldPath.fromString("subProperty")),
            HistoryDifferenceEventType.REMOVED,
            new HistoryValue("myLabel1", GenericEntityReference.of(TestData.class, 163l)),
            null);
    historyDifference4.setParentDifference(historyDifference3);
    historyDifference3.setDifferences(List.of(historyDifference4));

    TestHistoryLog historyLog =
        historyLogService.logNow(
            now,
            TestHistoryLogEventType.EVENT1,
            List.of(historyDifference1, historyDifference2, historyDifference3),
            new TestData(1l),
            new HistoryLogAdditionalInformationBean(
                new TestData(2l), new TestData(3l), new TestData(4l), new TestData(1l)));

    TestHistoryLog historyLogReload = historyLogService.getById(historyLog.getId());
    assertThat(historyLog).isNotSameAs(historyLogReload); // test sans entityManager global
    assertThat(historyLog.getDate()).isCloseTo(now, within(ofMillis(1)));
    assertThat(historyLog.getDifferences())
        .filteredOnAssertions(
            i -> {
              assertThat(i.getPath().getPath()).isEqualTo(FieldPath.fromString("property1"));
            })
        .anySatisfy(
            i -> {
              assertThat(i.getPath().getKey()).isNull();
              assertThat(i.getDifferences()).isEmpty();
              assertThat(i.getBefore().getSerialized()).isNull();
              assertThat(i.getBefore().getReference())
                  .isEqualTo(GenericEntityReference.of(TestData.class, 6l));
              assertThat(i.getAfter().getSerialized()).isNull();
              assertThat(i.getAfter().getReference())
                  .isEqualTo(GenericEntityReference.of(TestData.class, 7l));
              assertThat(i.getBefore().getLabel()).isEqualTo("myLabel1");
              assertThat(i.getAfter().getLabel()).isEqualTo("myLabel2");
              assertThat(i.getParentLog()).isEqualTo(historyLogReload);
              assertThat(i.getParentDifference()).isNull();
            })
        .isNotEmpty();
    assertThat(historyLog.getDifferences())
        .filteredOnAssertions(
            i -> {
              assertThat(i.getPath().getPath()).isEqualTo(FieldPath.fromString("property2"));
            })
        .anySatisfy(
            i -> {
              assertThat(i.getPath().getKey()).isNull();
              assertThat(i.getDifferences()).isEmpty();
              assertThat(i.getBefore().getSerialized()).isEqualTo("\"value1\"");
              assertThat(i.getBefore().getReference()).isNull();
              assertThat(i.getAfter().getSerialized()).isEqualTo("\"value2\"");
              assertThat(i.getAfter().getReference()).isNull();
              assertThat(i.getBefore().getLabel()).isEqualTo("myLabel1");
              assertThat(i.getAfter().getLabel()).isEqualTo("myLabel2");
              assertThat(i.getParentLog()).isEqualTo(historyLogReload);
              assertThat(i.getParentDifference()).isNull();
            });
    assertThat(historyLog.getDifferences())
        .filteredOnAssertions(
            i -> {
              assertThat(i.getPath().getPath()).isEqualTo(FieldPath.fromString("properties[]"));
            })
        .anySatisfy(
            i -> {
              assertThat(i.getPath().getKey().getLabel()).isEqualTo("myKey");
              assertThat(i.getPath().getKey().getSerialized()).isNull();
              assertThat(i.getPath().getKey().getReference())
                  .isEqualTo(GenericEntityReference.of(TestData.class, 11l));
              assertThat(i.getBefore().getSerialized()).isEqualTo("\"value1\"");
              assertThat(i.getBefore().getReference()).isNull();
              assertThat(i.getAfter().getSerialized()).isEqualTo("\"value2\"");
              assertThat(i.getAfter().getReference()).isNull();
              assertThat(i.getBefore().getLabel()).isEqualTo("myLabel1");
              assertThat(i.getAfter().getLabel()).isEqualTo("myLabel2");
              assertThat(i.getParentLog()).isEqualTo(historyLogReload);
              assertThat(i.getParentDifference()).isNull();

              assertThat(i.getDifferences())
                  .first()
                  .satisfies(
                      sub -> {
                        assertThat(sub.getPath().getPath())
                            .isEqualTo(FieldPath.fromString("subProperty"));
                        assertThat(sub.getPath().getKey()).isNull();
                        assertThat(sub.getDifferences()).isEmpty();
                        assertThat(sub.getBefore().getSerialized()).isNull();
                        assertThat(sub.getBefore().getReference())
                            .isEqualTo(GenericEntityReference.of(TestData.class, 163l));
                        assertThat(sub.getAfter()).isNotNull();
                        assertThat(sub.getAfter().getLabel()).isNull();
                        assertThat(sub.getAfter().getReference()).isNull();
                        assertThat(sub.getAfter().getSerialized()).isNull();
                        assertThat(sub.getBefore().getLabel()).isEqualTo("myLabel1");
                        // only first-level references historylog
                        assertThat(sub.getParentLog()).isNull();
                        assertThat(sub.getParentDifference()).isEqualTo(historyDifference3);
                      });
            });
    assertThat(historyLog.getEventType()).isEqualTo(TestHistoryLogEventType.EVENT1);
    assertThat(historyLog.getMainObject().getReference())
        .isEqualTo(GenericEntityReference.of(TestData.class, 1l));
    assertThat(historyLog.getObject1().getReference())
        .isEqualTo(GenericEntityReference.of(TestData.class, 2l));
    assertThat(historyLog.getObject2().getReference())
        .isEqualTo(GenericEntityReference.of(TestData.class, 3l));
    assertThat(historyLog.getObject3().getReference())
        .isEqualTo(GenericEntityReference.of(TestData.class, 4l));
    assertThat(historyLog.getObject4().getReference())
        .isEqualTo(GenericEntityReference.of(TestData.class, 1l));
    assertThat(historyLog.getSubject().getReference())
        .isEqualTo(GenericEntityReference.of(TestUser.class, 1l));
    assertThat(historyLog.getRootLog()).isEqualTo(historyLog);
  }

  @Test
  void testHistoryLogEventTypeMergeGroup() throws ServiceException, SecurityServiceException {
    transactionScopeIndependantRunnerService.run(
        false,
        () -> {
          historyLogService.log(
              TestHistoryLogEventType.EVENT1,
              new TestData(2l),
              HistoryLogAdditionalInformationBean.empty());
          historyLogService.log(
              TestHistoryLogEventType.EVENT1,
              new TestData(2l),
              HistoryLogAdditionalInformationBean.empty());
          historyLogService.log(
              TestHistoryLogEventType.EVENT2,
              new TestData(2l),
              HistoryLogAdditionalInformationBean.empty());
          historyLogService.log(
              TestHistoryLogEventType.EVENT2,
              new TestData(2l),
              HistoryLogAdditionalInformationBean.empty());
          historyLogService.log(
              TestHistoryLogEventType.EVENT3,
              new TestData(2l),
              HistoryLogAdditionalInformationBean.empty());
          return null;
        });

    Collection<TestHistoryLog> historyLogs =
        new JPAQuery<>(entityManager)
            .select(QTestHistoryLog.testHistoryLog)
            .from(QTestHistoryLog.testHistoryLog)
            .where(QTestHistoryLog.testHistoryLog.mainObject.reference.id.eq(2l))
            .orderBy(QTestHistoryLog.testHistoryLog.id.asc())
            .fetch();

    assertThat(historyLogs)
        .extracting(TestHistoryLog::getEventType)
        .containsExactly(TestHistoryLogEventType.EVENT1, TestHistoryLogEventType.EVENT2);

    transactionScopeIndependantRunnerService.run(
        false,
        () -> {
          historyLogService.logWithDifferences(
              TestHistoryLogEventType.EVENT1,
              new TestData(3l),
              HistoryLogAdditionalInformationBean.empty(),
              testDataDifferenceService);
          historyLogService.logWithDifferences(
              TestHistoryLogEventType.EVENT2,
              new TestData(3l),
              HistoryLogAdditionalInformationBean.empty(),
              testDataDifferenceService);
          historyLogService.log(
              TestHistoryLogEventType.EVENT2,
              new TestData(3l),
              HistoryLogAdditionalInformationBean.empty());
          historyLogService.logWithDifferences(
              TestHistoryLogEventType.EVENT3,
              new TestData(3l),
              HistoryLogAdditionalInformationBean.empty(),
              testDataDifferenceService);
          return null;
        });

    historyLogs =
        new JPAQuery<>(entityManager)
            .select(QTestHistoryLog.testHistoryLog)
            .from(QTestHistoryLog.testHistoryLog)
            .where(QTestHistoryLog.testHistoryLog.mainObject.reference.id.eq(3l))
            .orderBy(QTestHistoryLog.testHistoryLog.id.asc())
            .fetch();

    assertThat(historyLogs)
        .extracting(TestHistoryLog::getEventType)
        .containsExactly(
            TestHistoryLogEventType.EVENT2,
            TestHistoryLogEventType.EVENT1,
            TestHistoryLogEventType.EVENT2);
  }
}
