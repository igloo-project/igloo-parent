package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.factory.ComponentFactories;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreActionColumnLinkPanel;

import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.component.PlaceholderContainer;

public class ActionColumnLinkBuilder<T>
		extends AbstractActionColumnElementBuilder<T, AbstractDynamicBookmarkableLink, ActionColumnLinkBuilder<T>> {

	private static final long serialVersionUID = 1L;
	
	private boolean hideIfInvalid = false;
	
	public ActionColumnLinkBuilder(IBootstrapRenderer<? super T> renderer,
			ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
		super(renderer, ComponentFactories.fromLinkDescriptorMapper(mapper));
	}

	@Override
	public final MarkupContainer create(String wicketId, IModel<T> rowModel) {
		return new CoreActionColumnLinkPanel<T>(wicketId, rowModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected Component getLink(String string, IModel<T> rowModel) {
				AbstractDynamicBookmarkableLink link = getFactory().create("link", rowModel);
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

	@Override
	protected void decorateLink(AbstractDynamicBookmarkableLink link, IModel<T> rowModel) {
		super.decorateLink(link, rowModel);
		if (hideIfInvalid) {
			link.hideIfInvalid();
		}
	}
	
	@Override
	public void detach() {
		// nothing to do
	}

	public ActionColumnLinkBuilder<T> hideIfInvalid() {
		this.hideIfInvalid = true;
		return thisAsF();
	}

}
