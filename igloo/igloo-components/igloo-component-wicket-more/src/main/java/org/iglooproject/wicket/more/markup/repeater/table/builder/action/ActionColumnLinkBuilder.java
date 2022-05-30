package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import org.apache.wicket.model.IModel;
import org.iglooproject.bootstrap.api.renderer.IBootstrapRenderer;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.factory.ComponentFactories;

public class ActionColumnLinkBuilder<T>
		extends AbstractActionColumnElementBuilder<T, AbstractDynamicBookmarkableLink, ActionColumnLinkBuilder<T>> {

	private static final long serialVersionUID = 1L;
	
	private boolean hideIfInvalid = false;
	
	public ActionColumnLinkBuilder(IBootstrapRenderer<? super T> renderer,
			ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
		super(renderer, ComponentFactories.fromLinkDescriptorMapper(mapper));
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
