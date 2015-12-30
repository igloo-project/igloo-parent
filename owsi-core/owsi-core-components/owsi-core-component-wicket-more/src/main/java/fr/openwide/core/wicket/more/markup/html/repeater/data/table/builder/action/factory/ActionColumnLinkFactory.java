package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;

public class ActionColumnLinkFactory<T> extends AbstractActionColumnElementFactory<T, ActionColumnLinkFactory<T>> {

	private static final long serialVersionUID = 1L;
	
	private final IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper;
	
	private boolean hideIfInvalid = false;
	
	public ActionColumnLinkFactory(BootstrapRenderer<? super T> renderer, IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
		super(renderer);
		this.mapper = mapper;
	}
	
	@Override
	public AbstractLink create(String wicketId, IModel<T> parameter) {
		AbstractDynamicBookmarkableLink link = mapper.map(parameter)
				.link(wicketId);
		if (hideIfInvalid) {
			link.hideIfInvalid();
		}
		return link;
	}
	
	@Override
	public ActionColumnLinkFactory<T> thisAsF() {
		return this;
	}
	
	@Override
	public void detach() {
		// nothing to do
	}

	public ActionColumnLinkFactory<T> hideIfInvalid() {
		this.hideIfInvalid = true;
		return thisAsF();
	}

}
