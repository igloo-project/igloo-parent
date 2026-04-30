package test.jpa.more.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.model.TaskExecutionResult;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

// import tools.jackson.databind.ObjectMapper;

/**
 * Vérifie le contrat de {@code QueuedTaskHolder.updateExecutionInformation} (lecture du résultat,
 * de la stack trace, et sérialisation du report). La logique du test est identique avant et après
 * migration ; seul l'import {@code ObjectMapper} change de package (Jackson 2 → {@code
 * com.fasterxml.jackson.databind}, Jackson 3 → {@code tools.jackson.databind}).
 */
class QueuedTaskHolderTest {

  @Test
  void updateExecutionInformationCopiesResultAndSerializesReport() throws Exception {
    QueuedTaskHolder holder = new QueuedTaskHolder("task", "queue", "type", "{}");

    TaskExecutionResult result = mock(TaskExecutionResult.class);
    BatchReportBean report = mock(BatchReportBean.class);
    when(result.getResult()).thenReturn(TaskResult.SUCCESS);
    when(result.getStackTrace()).thenReturn("trace");
    when(result.getReport()).thenReturn(report);

    ObjectMapper mapper = mock(ObjectMapper.class);
    when(mapper.writeValueAsString(any())).thenReturn("{\"serialized\":true}");

    holder.updateExecutionInformation(result, mapper);

    assertThat(holder.getResult()).isEqualTo(TaskResult.SUCCESS);
    assertThat(holder.getStackTrace()).isEqualTo("trace");
    assertThat(holder.getReport()).isEqualTo("{\"serialized\":true}");
    verify(mapper).writeValueAsString(report);
  }

  @Test
  void updateExecutionInformationWithNullResultIsNoop() throws Exception {
    QueuedTaskHolder holder = new QueuedTaskHolder("task", "queue", "type", "{}");
    holder.setResult(TaskResult.WARN);
    holder.setStackTrace("previous");
    holder.setReport("previousReport");

    ObjectMapper mapper = mock(ObjectMapper.class);

    holder.updateExecutionInformation(null, mapper);

    assertThat(holder.getResult()).isEqualTo(TaskResult.WARN);
    assertThat(holder.getStackTrace()).isEqualTo("previous");
    assertThat(holder.getReport()).isEqualTo("previousReport");
  }
}
