package fr.openwide.core.jpa.more.config.spring.event;

import org.springframework.context.ApplicationEvent;

public class PropertyServiceInitEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1735401082005771273L;

	public PropertyServiceInitEvent(Object source) {
		super(source);
	}

}
