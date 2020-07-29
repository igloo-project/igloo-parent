package org.iglooproject.jpa.more.business.execution.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import com.google.common.base.MoreObjects.ToStringHelper;

@MappedSuperclass
@Bindable
@Table(
		indexes = {
				@Index(name = "idx_Execution_endDate", columnList = "endDate")
		}
)
public abstract class AbstractExecution<E extends GenericEntity<Long, E>, ET extends IExecutionType> extends GenericEntity<Long, E> {
	private static final long serialVersionUID = 6026078483841894077L;

	@GeneratedValue
	@Id
	private Long id;
	
	@Column
	private String name;
	
	@Column
	@Type(type = "org.iglooproject.jpa.hibernate.usertype.StringClobType")
	private String description;
	
	@Column
	@Type(type = "org.iglooproject.jpa.hibernate.usertype.StringClobType")
	private String logOutput;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ET executionType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ExecutionStatus executionStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	protected AbstractExecution() {
		super();
		setStartDate(new Date());
	}

	public AbstractExecution(ET executionType) {
		this();
		setExecutionType(executionType);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public ET getExecutionType() {
		return executionType;
	}

	public void setExecutionType(ET executionType) {
		this.executionType = executionType;
	}

	public ExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(ExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
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

	public void setEndDate(Date endDate) {
		this.endDate = CloneUtils.clone(endDate);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogOutput() {
		return logOutput;
	}

	public void setLogOutput(String logOutput) {
		this.logOutput = logOutput;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("executionType", getExecutionType());
	}

}
