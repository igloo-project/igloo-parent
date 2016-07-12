package fr.openwide.core.test.wicket.more.link.descriptor;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;

import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;
import fr.openwide.core.test.wicket.more.link.descriptor.page.TestLinkDescriptorNoParameterPage;
import fr.openwide.core.test.wicket.more.link.descriptor.page.TestLinkDescriptorOneParameterPage;
import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.factory.DetachableFactories;
import fr.openwide.core.wicket.more.markup.html.factory.ModelFactories;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class TestPageLinkDescriptorMapper extends AbstractAnyTargetTestLinkDescriptorMapper {

	@Override
	protected <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T> buildWithNullTarget(
			ILateTargetDefinitionTerminalState<
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder) {
		return builder.page(Model.of((Class<Page>) null));
	}
	
	@Override
	protected <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T> buildWithOneParameterTarget(
			ILateTargetDefinitionTerminalState<
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder) {
		return builder.page(TestLinkDescriptorOneParameterPage.class);
	}

	@Override
	protected <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T>
			buildWithParameterDependentNullTarget(IOneChosenParameterState<
					?,
					T,
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder) {
		return builder.page(ModelFactories.constant(Model.of((Class<Page>) null)));
	}
	
	@Override
	protected <T> IOneParameterLinkDescriptorMapper<? extends ILinkDescriptor, T>
			buildWithParameterDependentTarget(IOneChosenParameterState<
					?,
					T,
					IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IResourceLinkDescriptor, T>,
					IOneParameterLinkDescriptorMapper<IImageResourceLinkDescriptor, T>
					> builder) {
		return builder.page(DetachableFactories.forUnit(ReadOnlyModel.factory(
				new SerializableFunction<T, Class<? extends Page>>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Class<? extends Page> apply(T input) {
						return input == null ? TestLinkDescriptorNoParameterPage.class : TestLinkDescriptorOneParameterPage.class;
					}
				}
		)));
	}

	@Override
	protected String getNoParameterTargetPathPrefix() {
		return WicketMoreTestLinkDescriptorApplication.PAGE_NO_PARAMETER_PATH_PREFIX;
	}

	@Override
	protected String getOneParameterTargetPathPrefix() {
		return WicketMoreTestLinkDescriptorApplication.PAGE_ONE_PARAMETER_PATH_PREFIX;
	}
}
