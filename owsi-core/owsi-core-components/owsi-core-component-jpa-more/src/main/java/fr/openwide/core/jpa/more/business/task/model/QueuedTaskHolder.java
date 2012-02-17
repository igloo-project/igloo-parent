package fr.openwide.core.jpa.more.business.task.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.springframework.core.style.ToStringCreator;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@Entity
public class QueuedTaskHolder extends GenericEntity<Integer, QueuedTaskHolder> {
	private static final long serialVersionUID = 3926959721176678607L;

	@Id
	@GeneratedValue
	@DocumentId
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String taskType;

	@Column(nullable = false)
	private Date creationDate;

	private Date triggeringDate = null;

	private Date startDate = null;

	private Date completionDate = null;

	@Version
	@Column(name="optLock")
	private int version;

	@Column(nullable = false)
	@Type(type = "org.hibernate.type.StringClobType")
	private String serializedTask;

	protected QueuedTaskHolder() {
	}

	public QueuedTaskHolder(String name, String taskType, String serializedTask) {
		super();
		
		setName(name);
		setTaskType(taskType);
		setSerializedTask(serializedTask);
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
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

	public Date getCompletionDate() {
		return CloneUtils.clone(completionDate);
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = CloneUtils.clone(completionDate);
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

	@Override
	public String getNameForToString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
		return new ToStringCreator(this).append("id", getId())
				.append("name", getName())
				.append("creationDate", (getCreationDate() != null) ? dateFormat.format(getCreationDate()) : null)
				.append("startDate", (getStartDate() != null) ? dateFormat.format(getStartDate()) : null)
				.append("completionDate", (getCompletionDate() != null) ? dateFormat.format(getCompletionDate()) : null)
				.toString();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}
}
