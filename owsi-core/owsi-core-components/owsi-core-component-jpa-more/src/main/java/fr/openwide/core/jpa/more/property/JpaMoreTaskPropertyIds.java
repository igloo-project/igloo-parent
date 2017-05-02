package fr.openwide.core.jpa.more.property;

import java.util.Objects;

import fr.openwide.core.jpa.more.business.task.model.IQueueId;
import fr.openwide.core.spring.config.util.TaskQueueStartMode;
import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.ImmutablePropertyIdTemplate;

public final class JpaMoreTaskPropertyIds extends AbstractPropertyIds {
	
	private JpaMoreTaskPropertyIds() {
	}
	
	public static final ImmutablePropertyId<Integer> STOP_TIMEOUT = immutable("task.stop.timeout");
	public static final ImmutablePropertyId<TaskQueueStartMode> START_MODE = immutable("task.startMode");
	
	public static final ImmutablePropertyIdTemplate<Integer> QUEUE_NUMBER_OF_THREADS_TEMPLATE = immutableTemplate("task.queues.config.%1s.threads");
	public static final ImmutablePropertyId<Integer> queueNumberOfThreads(IQueueId queueId) {
		Objects.requireNonNull(queueId);
		return QUEUE_NUMBER_OF_THREADS_TEMPLATE.create(queueId.getUniqueStringId());
	}
	
	public static final ImmutablePropertyIdTemplate<Long> QUEUE_START_DELAY_TEMPLATE = immutableTemplate("task.queues.config.%1s.start.delay");
	public static final ImmutablePropertyId<Long> queueStartDelay(String queueId) {
		Objects.requireNonNull(queueId);
		return QUEUE_START_DELAY_TEMPLATE.create(queueId);
	}
	
	public static final ImmutablePropertyIdTemplate<Boolean> QUEUE_START_EXECUTION_CONTEXT_WAIT_READY_TEMPLATE = immutableTemplate("task.queues.config.%1s.start.executionContext.waitReady");
	public static final ImmutablePropertyId<Boolean> queueStartExecutionContextWaitReady(String queueId) {
		Objects.requireNonNull(queueId);
		return QUEUE_START_EXECUTION_CONTEXT_WAIT_READY_TEMPLATE.create(queueId);
	}

}
