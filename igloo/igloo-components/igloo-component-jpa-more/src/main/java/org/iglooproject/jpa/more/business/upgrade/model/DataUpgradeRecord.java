package org.iglooproject.jpa.more.business.upgrade.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.bindgen.Bindable;
import org.hibernate.annotations.NaturalId;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import com.google.common.base.MoreObjects.ToStringHelper;

@Entity
@Bindable
public class DataUpgradeRecord extends GenericEntity<Long, DataUpgradeRecord> {

	private static final long serialVersionUID = 6430150414026715207L;

	@Id
	@GeneratedValue
	private Long id;

	@NaturalId
	@Column(nullable = false)
	private String name;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date executionDate;

	@Column(nullable = false)
	private boolean autoPerform = false;

	@Column(nullable = false)
	private boolean done = false;

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

	public Date getExecutionDate() {
		return CloneUtils.clone(executionDate);
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = CloneUtils.clone(executionDate);
	}

	public boolean isAutoPerform() {
		return autoPerform;
	}

	public void setAutoPerform(boolean autoPerform) {
		this.autoPerform = autoPerform;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("name", getName());
	}

}
