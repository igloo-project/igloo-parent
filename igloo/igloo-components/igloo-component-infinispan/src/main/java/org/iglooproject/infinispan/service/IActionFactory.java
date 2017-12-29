package org.iglooproject.infinispan.service;

import org.iglooproject.infinispan.model.IAction;

public interface IActionFactory {

	void prepareAction(IAction<?> action);

}
