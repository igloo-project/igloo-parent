package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.action.IAjaxOneParameterAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class ActionColumnActionFactory<T> extends AbstractActionColumnElementFactory<T, ActionColumnActionFactory<T>> {

	private static final long serialVersionUID = 1L;
	
	private final IAjaxOneParameterAction<IModel<T>> action;
	
	public ActionColumnActionFactory(BootstrapLabelRenderer<? super T> renderer, IAjaxOneParameterAction<IModel<T>> action) {
		super(renderer);
		this.action = action;
	}
	
	@Override
	public AbstractLink create(String wicketId, final IModel<T> parameter) {
		return new Link<T>(wicketId, parameter) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				action.execute(parameter);
			}
		};
	}
	
	@Override
	public ActionColumnActionFactory<T> thisAsF() {
		return this;
	}
	
	@Override
	public void detach() {
		Detachables.detach(action);
	}

}
