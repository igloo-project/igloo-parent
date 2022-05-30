package test.wicket.more.link.descriptor;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.api.model.ModelFactories;
import org.iglooproject.wicket.api.model.ReadOnlyModel;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;

import test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;
import test.wicket.more.link.descriptor.page.TestLinkDescriptorNoParameterPage;
import test.wicket.more.link.descriptor.page.TestLinkDescriptorOneParameterPage;

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
				input -> input == null ? TestLinkDescriptorNoParameterPage.class : TestLinkDescriptorOneParameterPage.class
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
