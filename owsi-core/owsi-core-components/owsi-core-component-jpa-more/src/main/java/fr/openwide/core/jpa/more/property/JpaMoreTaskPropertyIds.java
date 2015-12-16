package fr.openwide.core.jpa.more.property;

import java.util.Objects;

import fr.openwide.core.spring.config.util.TaskQueueStartMode;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.ImmutablePropertyIdTemplate;

public final class JpaMoreTaskPropertyIds {

	public static final ImmutablePropertyId<Integer> STOP_TIMEOUT = new ImmutablePropertyId<>("task.stop.timeout");
	public static final ImmutablePropertyId<TaskQueueStartMode> START_MODE = new ImmutablePropertyId<>("task.startMode");
	public static final ImmutablePropertyIdTemplate<Integer> QUEUE_NUMBER_OF_THREADS_TEMPLATE = new ImmutablePropertyIdTemplate<>("task.queues.config.%1s.threads");
	public static final ImmutablePropertyId<Integer> queueNumberOfThreads(String queueId) {
		Objects.requireNonNull(queueId);
		return QUEUE_NUMBER_OF_THREADS_TEMPLATE.create(queueId);
	}

}
