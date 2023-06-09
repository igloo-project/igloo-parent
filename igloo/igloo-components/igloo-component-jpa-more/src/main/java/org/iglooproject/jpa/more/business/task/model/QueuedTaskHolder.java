package org.iglooproject.jpa.more.business.task.model;

import java.time.Instant;

import org.bindgen.Bindable;
import org.hibernate.annotations.JavaType;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.task.util.TaskResult;
import org.iglooproject.jpa.more.business.task.util.TaskStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects.ToStringHelper;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

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

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@FullTextField(name = NAME, analyzer = HibernateSearchAnalyzer.TEXT)
	@KeywordField(name = NAME_SORT, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
	@JavaType(StringJavaType.class)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private String name;

	@Column(nullable = true)
	@GenericField(name = QUEUE_ID)
	private String queueId;

	@Column(nullable = false)
	@GenericField(name = TASK_TYPE)
	private String taskType;

	@Column(nullable = false)
	@GenericField(name = CREATION_DATE, sortable = Sortable.YES)
	private Instant creationDate;

	@GenericField(name = TRIGGERING_DATE, sortable = Sortable.YES)
	private Instant triggeringDate = null;

	@GenericField(name = START_DATE, sortable = Sortable.YES)
	private Instant startDate = null;

	@GenericField(name = END_DATE, sortable = Sortable.YES)
	private Instant endDate = null;

	@Version
	@Column(name = "optLock")
	private int version;

	@Column(nullable = false)
	@JavaType(StringJavaType.class)
	private String serializedTask;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@GenericField(name = STATUS)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private TaskStatus status;
	
	@Column
	@Enumerated(EnumType.STRING)
	@GenericField(name = RESULT)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private TaskResult result;

	@Column
	@JavaType(StringJavaType.class)
	private String stackTrace;

	@Column
	@JavaType(StringJavaType.class)
	private String report;

	protected QueuedTaskHolder() {
	}

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

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	public Instant getTriggeringDate() {
		return triggeringDate;
	}

	public void setTriggeringDate(Instant triggeringDate) {
		this.triggeringDate = triggeringDate;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
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
	public void updateExecutionInformation(TaskExecutionResult executionResult, ObjectMapper objectMapper)
			throws JsonProcessingException {
		if (executionResult != null) {
			setResult(executionResult.getResult());
			setStackTrace(executionResult.getStackTrace());
			setReport(objectMapper.writeValueAsString(executionResult.getReport()));
		}
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("name", getName())
			.add("creationDate", getCreationDate())
			.add("startDate", getStartDate())
			.add("completionDate", getEndDate());
	}

}
