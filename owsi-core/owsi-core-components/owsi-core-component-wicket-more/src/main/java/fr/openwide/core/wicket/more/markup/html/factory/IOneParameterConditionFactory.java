package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.model.IDetachable;

import fr.openwide.core.wicket.more.condition.Condition;

public interface IOneParameterConditionFactory<T> extends IDetachable {

	Condition create(T parameter);

}
