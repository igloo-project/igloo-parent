package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IPageInstanceState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.ITerminalState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.impl.CorePageInstanceLinkGenerator;
import fr.openwide.core.wicket.more.model.ClassModel;

public class CoreLinkDescriptorBuilderPageInstanceStateImpl implements IPageInstanceState<IPageLinkGenerator> {
	
	private final IModel<? extends Page> pageInstanceModel;
	
	private IModel<? extends Class<? extends Page>> expectedPageClassModel;

	public CoreLinkDescriptorBuilderPageInstanceStateImpl(IModel<? extends Page> pageInstanceModel) {
		this.pageInstanceModel = pageInstanceModel;
		this.expectedPageClassModel = null;
	}

	@Override
	public <P extends Page> ITerminalState<IPageLinkGenerator> validate(Class<P> expectedPageClass) {
		return validate(ClassModel.of(expectedPageClass));
	}

	@Override
	public ITerminalState<IPageLinkGenerator> validate(IModel<? extends Class<? extends Page>> expectedPageClassModel) {
		this.expectedPageClassModel = expectedPageClassModel;
		return this;
	}

	@Override
	public IPageLinkGenerator build() {
		return new CorePageInstanceLinkGenerator(pageInstanceModel, expectedPageClassModel);
	}

}
