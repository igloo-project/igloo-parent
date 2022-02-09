package org.iglooproject.wicket.more.markup.repeater.table.builder.action.factory;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.action.IOneParameterAction;
import org.iglooproject.wicket.api.factory.IOneParameterComponentFactory;

public class ActionColumnActionFactory<T> implements IOneParameterComponentFactory<Link<T>, IModel<T>> {

	private static final long serialVersionUID = 1L;
	
	private final IOneParameterAction<? super IModel<T>> action;
	
	public ActionColumnActionFactory(IOneParameterAction<? super IModel<T>> action) {
		super();
		this.action = action;
	}
	
	@Override
	public Link<T> create(String wicketId, final IModel<T> parameter) {
		return new Link<T>(wicketId, parameter) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				action.execute(parameter);
			}
		};
	}
	
	@Override
	public void detach() {
		action.detach();
	}

}
