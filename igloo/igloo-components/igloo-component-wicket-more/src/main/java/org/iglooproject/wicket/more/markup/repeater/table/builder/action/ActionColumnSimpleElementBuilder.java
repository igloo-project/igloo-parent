package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreActionColumnSimpleElementPanel;

import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.factory.IOneParameterComponentFactory;

public class ActionColumnSimpleElementBuilder<T, L extends AbstractLink>
		extends AbstractActionColumnElementBuilder<T, L, ActionColumnSimpleElementBuilder<T, L>> {

	private static final long serialVersionUID = 1L;

	public ActionColumnSimpleElementBuilder(IBootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<? extends L, IModel<T>> factory) {
		super(renderer, factory);
	}

	@Override
	public final MarkupContainer create(String wicketId, IModel<T> rowModel) {
		return new CoreActionColumnSimpleElementPanel<T>(wicketId, rowModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected Component getLink(String string, IModel<T> rowModel) {
				L link = getFactory().create("link", rowModel);
				decorateLink(link, rowModel);
				return link;
			}
			@Override
			protected PlaceholderContainer getPlaceholder(String string, IModel<T> rowModel) {
				PlaceholderContainer placeholder = new PlaceholderContainer("linkPlaceholder");
				decoratePlaceholder(placeholder, rowModel);
				return placeholder;
			}
		};
	}

}
