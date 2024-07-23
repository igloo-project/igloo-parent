package org.iglooproject.jpa.more.business.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects.ToStringHelper;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;

@Entity
@Bindable
@Indexed
public class QueuedTaskHolder extends GenericEntity<Long, QueuedTaskHolder> {

  private static final long serialVersionUID = 3926959721176678607L;

  public static final String NAME = "name";
  public static final String NAME_SORT = "nameSort";

  public static final String QUEUE_ID = "queueId";

  public static final String TASK_TYPE = "taskType";

  public static final String CREATION_DATE = "creationDate";
  public static final String TRIGGERING_DATE = "triggeringDate";
  public static final String START_DATE = "startDate";
  public static final String END_DATE = "endDate";

  public static final String STATUS = "status";

  public static final String RESULT = "result";

  @Id @GeneratedValue @DocumentId private Long id;

  @Column(nullable = false)
  @Field(name = NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
  @Field(name = NAME_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
  @SortableField(forField = NAME_SORT)
  @Type(type = "text")
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private String name;

  @Column(nullable = true)
  @Field(
      name = QUEUE_ID,
      analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD),
      indexNullAs = Field.DEFAULT_NULL_TOKEN)
  private String queueId;

  @Column(nullable = false)
  @Field(name = TASK_TYPE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
  private String taskType;

  @Column(nullable = false)
  @Field(name = CREATION_DATE)
  @SortableField(forField = CREATION_DATE)
  private Date creationDate;

  @Field(name = TRIGGERING_DATE)
  @SortableField(forField = TRIGGERING_DATE)
  private Date triggeringDate = null;

  @Field(name = START_DATE)
  @SortableField(forField = START_DATE)
  private Date startDate = null;

  @Field(name = END_DATE)
  @SortableField(forField = END_DATE)
  private Date endDate = null;

  @Version
  @Column(name = "optLock")
  private int version;

  @Column(nullable = false)
  @Type(type = "text")
  private String serializedTask;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Field(name = STATUS, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private TaskStatus status;

  @Column
  @Enumerated(EnumType.STRING)
  @Field(name = RESULT, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
  @SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
  private TaskResult result;

  @Column
  @Type(type = "text")
  private String stackTrace;

  @Column
  @Type(type = "text")
  private String report;

  protected QueuedTaskHolder() {}

  public QueuedTaskHolder(String name, String queueId, String taskType, String serializedTask) {
    super();
    setName(name);
    setQueueId(queueId);
    setTaskType(taskType);
    setSerializedTask(serializedTask);
    setStatus(TaskStatus.TO_RUN);
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getQueueId() {
    return queueId;
  }

  public void setQueueId(String queueId) {
    this.queueId = queueId;
  }

  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  public Date getCreationDate() {
    return CloneUtils.clone(creationDate);
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = CloneUtils.clone(creationDate);
  }

  public Date getTriggeringDate() {
    return CloneUtils.clone(triggeringDate);
  }

  public void setTriggeringDate(Date triggeringDate) {
    this.triggeringDate = CloneUtils.clone(triggeringDate);
  }

  public Date getStartDate() {
    return CloneUtils.clone(startDate);
  }

  public void setStartDate(Date startDate) {
    this.startDate = CloneUtils.clone(startDate);
  }

  public Date getEndDate() {
    return CloneUtils.clone(endDate);
  }

  public void setEndDate(Date completionDate) {
    this.endDate = CloneUtils.clone(completionDate);
  }

  public String getSerializedTask() {
    return serializedTask;
  }

  public void setSerializedTask(String serializedTask) {
    this.serializedTask = serializedTask;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public TaskResult getResult() {
    return result;
  }

  public void setResult(TaskResult result) {
    this.result = result;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }

  public String getReport() {
    return report;
  }

  public void setReport(String report) {
    this.report = report;
  }

  @JsonIgnore
  public void resetExecutionInformation() {
    setResult(null);
    setStackTrace(null);
    setReport(null);
  }

  @JsonIgnore
  public void updateExecutionInformation(
      TaskExecutionResult executionResult, ObjectMapper objectMapper)
      throws JsonProcessingException {
    if (executionResult != null) {
      setResult(executionResult.getResult());
      setStackTrace(executionResult.getStackTrace());
      setReport(objectMapper.writeValueAsString(executionResult.getReport()));
    }
  }

  @Override
  protected ToStringHelper toStringHelper() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
    return super.toStringHelper()
        .add("name", getName())
        .add(
            "creationDate", getCreationDate() != null ? dateFormat.format(getCreationDate()) : null)
        .add("startDate", getStartDate() != null ? dateFormat.format(getStartDate()) : null)
        .add("completionDate", getEndDate() != null ? dateFormat.format(getEndDate()) : null);
  }
}
