package fr.openwide.core.infinispan.service;

import fr.openwide.core.infinispan.model.IAction;

public interface IActionFactory {

	void prepareAction(IAction<?> action);

}
