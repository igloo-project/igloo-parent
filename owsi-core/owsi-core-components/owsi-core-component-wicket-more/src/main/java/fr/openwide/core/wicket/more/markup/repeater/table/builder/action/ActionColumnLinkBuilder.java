package fr.openwide.core.wicket.more.markup.repeater.table.builder.action;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.ComponentFactories;

public class ActionColumnLinkBuilder<T>
		extends AbstractActionColumnElementBuilder<T, AbstractDynamicBookmarkableLink, ActionColumnLinkBuilder<T>> {

	private static final long serialVersionUID = 1L;
	
	private boolean hideIfInvalid = false;
	
	public ActionColumnLinkBuilder(BootstrapRenderer<? super T> renderer,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
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
