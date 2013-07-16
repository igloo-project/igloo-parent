package fr.openwide.core.jpa.more.business.task.model;

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
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.springframework.core.style.ToStringCreator;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.task.util.TaskStatus;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

@Entity
@Bindable
@Indexed
public class QueuedTaskHolder extends GenericEntity<Long, QueuedTaskHolder> {
	private static final long serialVersionUID = 3926959721176678607L;

	public static final String NAME_SORT_FIELD_NAME = "nameSort";

	@Id
	@GeneratedValue
	@DocumentId
	private Long id;

	@Column(nullable = false)
	@Fields({ 
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
		@Field(name = NAME_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	private String name;

	@Column(nullable = false)
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private String taskType;

	@Column(nullable = false)
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Date creationDate;

	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Date triggeringDate = null;

	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Date startDate = null;

	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Date endDate = null;

	@Version
	@Column(name = "optLock")
	private int version;

	@Column(nullable = false)
	@Type(type = "org.hibernate.type.StringClobType")
	private String serializedTask;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private TaskStatus status;

	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	private String result;

	protected QueuedTaskHolder() {
	}

	public QueuedTaskHolder(String name, String taskType, String serializedTask) {
		super();
		setName(name);
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String getNameForToString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
		return new ToStringCreator(this)
				.append("id", getId())
				.append("name", getName())
				.append("creationDate", (getCreationDate() != null) ? dateFormat.format(getCreationDate()) : null)
				.append("startDate", (getStartDate() != null) ? dateFormat.format(getStartDate()) : null)
				.append("completionDate", (getEndDate() != null) ? dateFormat.format(getEndDate()) : null)
				.toString();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}
}
