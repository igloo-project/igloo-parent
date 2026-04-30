package test.jpa.more.business;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import org.iglooproject.commons.util.report.BatchReport;
import org.iglooproject.commons.util.report.BatchReportItem;
import org.iglooproject.commons.util.report.BatchReportItemSeverity;
import org.iglooproject.jpa.more.autoconfigure.TaskAutoConfiguration;
import org.iglooproject.jpa.more.business.task.model.AbstractTask;
import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.business.task.model.TaskExecutionResult;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import test.jpa.more.business.task.config.TestTaskManagementConfig;
import test.jpa.more.config.SpringBootTestJpaMore;

/**
 * Tests Jackson serialization/deserialization of task-related objects using the real {@link
 * TaskAutoConfiguration#queuedTaskHolderObjectMapper()} bean. Validates date format, enum format,
 * polymorphic type handling, and round-trip serialization.
 */
@SpringBootTestJpaMore
@ContextConfiguration(classes = TestTaskManagementConfig.class)
class QueuedTaskHolderJacksonTest extends AbstractJpaMoreTestCase {

  @Autowired
  @Qualifier(TaskAutoConfiguration.OBJECT_MAPPER_BEAN_NAME)
  private ObjectMapper objectMapper;

  /** Concrete task for serialization testing. */
  public static class TestSerializationTask extends AbstractTask {
    private static final long serialVersionUID = 1L;

    private String label;
    private int count;
    private TaskResult status;

    protected TestSerializationTask() {}

    public TestSerializationTask(String label, int count, TaskResult status) {
      super("testTask", "testType", new Date(1700000000000L));
      this.label = label;
      this.count = count;
      this.status = status;
    }

    @Override
    protected TaskExecutionResult doTask() {
      return TaskExecutionResult.completed();
    }

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public TaskResult getStatus() {
      return status;
    }

    public void setStatus(TaskResult status) {
      this.status = status;
    }
  }

  @Test
  void taskRoundTrip() throws Exception {
    TestSerializationTask task = new TestSerializationTask("test-label", 42, TaskResult.SUCCESS);

    String json = objectMapper.writeValueAsString(task);
    AbstractTask deserialized = objectMapper.readValue(json, AbstractTask.class);

    assertThat(deserialized).isInstanceOf(TestSerializationTask.class);
    TestSerializationTask result = (TestSerializationTask) deserialized;
    assertThat(result.getLabel()).isEqualTo("test-label");
    assertThat(result.getCount()).isEqualTo(42);
    assertThat(result.getStatus()).isEqualTo(TaskResult.SUCCESS);
    assertThat(result.getTriggeringDate()).isEqualTo(new Date(1700000000000L));
  }

  @Test
  void taskDateSerializedAsTimestamp() throws Exception {
    TestSerializationTask task = new TestSerializationTask("test", 0, TaskResult.SUCCESS);

    String json = objectMapper.writeValueAsString(task);
    JsonNode tree = objectMapper.readTree(json);

    // DefaultTyping.NON_FINAL wraps the root object: ["className", {fields...}]
    JsonNode objectNode = tree.isArray() ? tree.get(1) : tree;

    // With Jackson 2 + JavaTimeModule defaults, Date is serialized as timestamp (long)
    JsonNode triggeringDateNode = objectNode.get("triggeringDate");
    assertThat(triggeringDateNode).as("triggeringDate field in JSON: %s", json).isNotNull();
    // The value may be wrapped in a type array too: ["java.util.Date", 1700000000000]
    long timestamp =
        triggeringDateNode.isArray()
            ? triggeringDateNode.get(1).longValue()
            : triggeringDateNode.longValue();
    assertThat(timestamp).isEqualTo(1700000000000L);
  }

  @Test
  void taskEnumSerializedAsName() throws Exception {
    TestSerializationTask task = new TestSerializationTask("test", 0, TaskResult.WARN);

    String json = objectMapper.writeValueAsString(task);

    // With Jackson 2 defaults, enums are serialized as name (not ordinal, not toString)
    assertThat(json).contains("\"WARN\"");
  }

  @Test
  void taskPolymorphicTypeInfo() throws Exception {
    TestSerializationTask task = new TestSerializationTask("test", 0, TaskResult.SUCCESS);

    String json = objectMapper.writeValueAsString(task);

    // DefaultTyping.NON_FINAL adds type information
    assertThat(json).contains(TestSerializationTask.class.getName());
  }

  private static BatchReportBean createReportWithItems() {
    BatchReport batchReport = new BatchReport();
    batchReport.info("Item processed successfully");
    batchReport.warn("Duplicate entry detected");
    batchReport.error("Failed to process item");
    return new BatchReportBean(batchReport);
  }

  @Test
  void reportRoundTrip() throws Exception {
    BatchReportBean report = createReportWithItems();

    String json = objectMapper.writeValueAsString(report);
    BatchReportBean deserialized = objectMapper.readValue(json, BatchReportBean.class);

    assertThat(deserialized.isOnError()).isTrue();
    assertThat(deserialized.getItems()).isNotEmpty();

    List<BatchReportItem> items = deserialized.getItems().get(BatchReport.GLOBAL_CONTEXT);
    assertThat(items).hasSize(3);
    assertThat(items.get(0).getMessage()).isEqualTo("Item processed successfully");
    assertThat(items.get(0).getSeverity()).isEqualTo(BatchReportItemSeverity.INFO);
    assertThat(items.get(0).getInstant()).isNotNull();
    assertThat(items.get(1).getSeverity()).isEqualTo(BatchReportItemSeverity.WARN);
    assertThat(items.get(2).getSeverity()).isEqualTo(BatchReportItemSeverity.ERROR);
  }

  /** Simulates the real flow: serialize task → store in holder → deserialize from holder. */
  @Test
  void taskSerializeDeserializeThroughHolder() throws Exception {
    TestSerializationTask task = new TestSerializationTask("flow-test", 7, TaskResult.ERROR);

    String serializedTask = objectMapper.writeValueAsString(task);
    QueuedTaskHolder holder = new QueuedTaskHolder("task", "queue", "testType", serializedTask);

    // Deserialize like ConsumerThread does
    AbstractTask deserialized =
        objectMapper.readValue(holder.getSerializedTask(), AbstractTask.class);

    assertThat(deserialized).isInstanceOf(TestSerializationTask.class);
    TestSerializationTask result = (TestSerializationTask) deserialized;
    assertThat(result.getLabel()).isEqualTo("flow-test");
    assertThat(result.getCount()).isEqualTo(7);
    assertThat(result.getStatus()).isEqualTo(TaskResult.ERROR);
    assertThat(result.getTriggeringDate()).isEqualTo(new Date(1700000000000L));
  }

  @Test
  void updateExecutionInformationWithRealMapper() throws Exception {
    QueuedTaskHolder holder = new QueuedTaskHolder("task", "queue", "type", "{}");

    BatchReportBean report = createReportWithItems();
    TaskExecutionResult result = TaskExecutionResult.completed(report);

    holder.updateExecutionInformation(result, objectMapper);

    assertThat(holder.getResult()).isEqualTo(TaskResult.ERROR);
    assertThat(holder.getReport()).isNotNull();

    // Verify report can be deserialized back with all items intact
    BatchReportBean deserializedReport =
        objectMapper.readValue(holder.getReport(), BatchReportBean.class);
    assertThat(deserializedReport.isOnError()).isTrue();

    List<BatchReportItem> items = deserializedReport.getItems().get(BatchReport.GLOBAL_CONTEXT);
    assertThat(items).hasSize(3);
    assertThat(items.get(0).getMessage()).isEqualTo("Item processed successfully");
    assertThat(items.get(0).getSeverity()).isEqualTo(BatchReportItemSeverity.INFO);
    assertThat(items.get(0).getInstant()).isNotNull();
    assertThat(items.get(2).getSeverity()).isEqualTo(BatchReportItemSeverity.ERROR);
  }

  @Override
  protected void cleanAll() {}
}
