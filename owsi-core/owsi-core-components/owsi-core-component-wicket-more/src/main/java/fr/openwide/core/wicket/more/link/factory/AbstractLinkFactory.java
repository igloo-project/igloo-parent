package fr.openwide.core.wicket.more.link.factory;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public abstract class AbstractLinkFactory {

	protected IBaseState builder() {
		return new LinkDescriptorBuilder();
	}
	
	public IPageLinkGenerator linkGenerator(Page pageInstance, Class<? extends Page> expectedClass) {
		return builder().pageInstance(pageInstance).validate(expectedClass).build();
	}
	
	public IPageLinkGenerator linkGenerator(IModel<? extends Page> pageInstanceModel, Class<? extends Page> expectedClass) {
		return builder().pageInstance(pageInstanceModel).validate(expectedClass).build();
	}

}