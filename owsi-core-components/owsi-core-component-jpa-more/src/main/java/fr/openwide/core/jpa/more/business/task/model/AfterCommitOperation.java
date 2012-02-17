package fr.openwide.core.jpa.more.business.task.model;

import org.springframework.beans.factory.annotation.Configurable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.util.transaction.model.IAfterCommitOperation;

@Configurable
public abstract class AfterCommitOperation implements IAfterCommitOperation {

	private GenericEntity<?, ?> entity;

	private OperationType operation;

	public AfterCommitOperation(GenericEntity<?, ?> entity, OperationType operation) {
		this.entity = entity;
		this.operation = operation;
	}

	public GenericEntity<?, ?> getEntity() {
		return entity;
	}

	public void setEntity(GenericEntity<?, ?> entity) {
		this.entity = entity;
	}

	public OperationType getOperation() {
		return operation;
	}

	public void setOperation(OperationType operation) {
		this.operation = operation;
	}

	public String getTaskName() {
		StringBuilder taskName = new StringBuilder();
		taskName.append(operation).append("-");
		taskName.append(entity.getClass().getSimpleName()).append("-");
		taskName.append(entity.getId());
		
		return taskName.toString();
	}

	@Override
	public String toString() {
		return getTaskName();
	}
}
