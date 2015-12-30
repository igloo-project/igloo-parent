package fr.openwide.core.wicket.more.markup.html.action;

import org.apache.wicket.model.IDetachable;

public interface IOneParameterAction<T> extends IDetachable {

	public void execute(T parameter);

}
