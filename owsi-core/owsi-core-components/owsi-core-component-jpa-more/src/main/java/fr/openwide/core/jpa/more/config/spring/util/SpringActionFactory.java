package fr.openwide.core.jpa.more.config.spring.util;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.infinispan.model.IAction;
import fr.openwide.core.infinispan.service.IActionFactory;

public class SpringActionFactory implements IActionFactory {

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void prepareAction(IAction<?> action) {
		applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(action, Autowire.BY_TYPE.value(), true);
	}

}
