package fr.openwide.core.wicket.more.markup.repeater.table.builder.action;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;

public class ActionColumnSimpleElementBuilder<T, L extends AbstractLink>
		extends AbstractActionColumnElementBuilder<T, L, ActionColumnSimpleElementBuilder<T, L>> {

	private static final long serialVersionUID = 1L;

	public ActionColumnSimpleElementBuilder(BootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<? extends L, IModel<T>> factory) {
		super(renderer, factory);
	}

}
