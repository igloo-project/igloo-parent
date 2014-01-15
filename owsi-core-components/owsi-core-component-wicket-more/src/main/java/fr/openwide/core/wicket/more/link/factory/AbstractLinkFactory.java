package fr.openwide.core.wicket.more.link.factory;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IPageInstanceState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.model.PageModel;

public abstract class AbstractLinkFactory {

	protected IBaseState builder() {
		return new LinkDescriptorBuilder();
	}
	
	public IPageLinkGenerator linkGenerator(Page pageInstance, Class<? extends Page> expectedClass,
			Class<? extends Page>... otherExpectedClasses) {
		return linkGenerator(PageModel.of(pageInstance), expectedClass, otherExpectedClasses);
	}
	
	public IPageLinkGenerator linkGenerator(IModel<? extends Page> pageInstanceModel,
			Class<? extends Page> expectedClass,
			Class<? extends Page>... otherExpectedClasses) {
		IPageInstanceState<?> builder = builder().pageInstance(pageInstanceModel).validate(expectedClass);
		for (Class<? extends Page> otherExpectedClass : otherExpectedClasses) {
			builder.validate(otherExpectedClass);
		}
		return builder.build();
	}

}