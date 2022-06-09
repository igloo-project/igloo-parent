package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.factory.IOneParameterComponentFactory;

public class ActionColumnSimpleElementBuilder<T, L extends AbstractLink>
		extends AbstractActionColumnElementBuilder<T, L, ActionColumnSimpleElementBuilder<T, L>> {

	private static final long serialVersionUID = 1L;

	public ActionColumnSimpleElementBuilder(IBootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<? extends L, IModel<T>> factory) {
		super(renderer, factory);
	}

}
