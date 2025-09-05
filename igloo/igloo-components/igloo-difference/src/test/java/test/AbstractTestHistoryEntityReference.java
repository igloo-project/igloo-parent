package test;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.Instant;
import java.util.List;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryDifferencePath;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.business.model.HistoryLogAdditionalInformationBean;
import test.business.model.TestData;
import test.business.model.TestHistoryDifference;
import test.business.model.TestHistoryEventType;
import test.business.model.TestHistoryLog;
import test.business.model.TestUser;
import test.business.service.ISubjectService;
import test.business.service.ITestHistoryLogService;

@SpringBootTestDifference
abstract class AbstractTestHistoryEntityReference {
  @Autowired ITestHistoryLogService historyLogService;

  @Autowired ISubjectService subjectService;

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
            TestHistoryEventType.EVENT1,
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
    assertThat(historyLog.getEventType()).isEqualTo(TestHistoryEventType.EVENT1);
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
}
