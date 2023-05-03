package org.iglooproject.jpa.more.business.upgrade.model;

import java.time.Instant;

import org.bindgen.Bindable;
import org.hibernate.annotations.NaturalId;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import com.google.common.base.MoreObjects.ToStringHelper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
	private Instant executionDate;

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

	public Instant getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Instant executionDate) {
		this.executionDate = executionDate;
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
