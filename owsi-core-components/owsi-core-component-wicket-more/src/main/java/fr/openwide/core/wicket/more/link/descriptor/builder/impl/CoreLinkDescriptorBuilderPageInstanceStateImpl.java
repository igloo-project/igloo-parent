package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import java.util.Set;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Sets;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IPageInstanceState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.impl.CorePageInstanceLinkGenerator;
import fr.openwide.core.wicket.more.model.ClassModel;

public class CoreLinkDescriptorBuilderPageInstanceStateImpl implements IPageInstanceState<IPageLinkGenerator> {
	
	private final IModel<? extends Page> pageInstanceModel;
	
	private Set<IModel<? extends Class<? extends Page>>> expectedPageClassModels = Sets.newHashSet();

	public CoreLinkDescriptorBuilderPageInstanceStateImpl(IModel<? extends Page> pageInstanceModel) {
		this.pageInstanceModel = pageInstanceModel;
	}

	@Override
	public <P extends Page> IPageInstanceState<IPageLinkGenerator> validate(Class<P> expectedPageClass) {
		return validate(ClassModel.of(expectedPageClass));
	}

	@Override
	public IPageInstanceState<IPageLinkGenerator> validate(IModel<? extends Class<? extends Page>> expectedPageClassModel) {
		expectedPageClassModels.add(expectedPageClassModel);
		return this;
	}

	@Override
	public IPageLinkGenerator build() {
		return new CorePageInstanceLinkGenerator(pageInstanceModel, expectedPageClassModels);
	}

}
