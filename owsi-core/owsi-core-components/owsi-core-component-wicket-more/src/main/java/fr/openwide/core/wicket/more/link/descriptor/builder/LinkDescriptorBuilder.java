package fr.openwide.core.wicket.more.link.descriptor.builder;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.BuilderLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.BuilderTargetFactories;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.main.NoMappableParameterMainStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.pageinstance.CoreLinkDescriptorBuilderPageInstanceStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.base.IBaseState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.pageinstance.IPageInstanceState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IEarlyTargetDefinitionTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.model.PageModel;

/**
 * A utility class for easily building link descriptors or link descriptor mappers with a fluent, type-safe API.
 * <p>This class allows in particular to define:
 * <ul>
 * <li>The link target (page or resource)
 * <li>The link parameters (models) and their mapping (HTTP query parameter name)
 * <li>The link validations
 * </ul>
 * <p><strong>Beware:</strong>Due to backward-compatibility constraints, target definition may get a bit confusing.
 * For new uses of this class, please use late target definition: begin your building code with
 * <code>LinkDescriptorBuilder.start()</code> and finish in with one of the methods in
 * {@link ILateTargetDefinitionTerminalState}. Refrain from calling {@link IEarlyTargetDefinitionTerminalState#build()},
 * since it will only return an unusable link descriptor or mapper.
 * <p>You may encounter legacy code using {@link LinkDescriptorBuilder}'s constructor and
 * {@link IEarlyTargetDefinitionTerminalState#build()}. This kind of use is called "early target definition" in this
 * API's javadoc, and is deprecated because it does not allow defining dynamic targets based on parameters.
 */
@SuppressWarnings("deprecation")
public class LinkDescriptorBuilder implements IBaseState {
	
	public static INoMappableParameterMainState<
			Void,
			IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
			> start() {
		return new NoMappableParameterMainStateImpl<>(BuilderTargetFactories.late());
	}
	
	public static IPageInstanceState<IPageLinkGenerator> start(Page page) {
		return start(new PageModel<>(page));
	}
	
	public static IPageInstanceState<IPageLinkGenerator> start(IModel<? extends Page> pageInstanceModel) {
		return new CoreLinkDescriptorBuilderPageInstanceStateImpl(pageInstanceModel);
	}
	
	/**
	 * @deprecated This constructor is the deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper.
	 * Use {@link #start()} instead (or {@link #start(Page)}, or {@link #start(IModel)}, then define your parameters
	 * (if any), and only when you're done use one of the methods defined in {@link ILateTargetDefinitionTerminalState}
	 * for defining the link's target and retrieving your link descriptor or link descriptor mapper. Other methods for
	 * <em>parameterized</em> late target definition are defined in {@link IChosenParameterState}.
	 */
	@Deprecated
	public LinkDescriptorBuilder() { }

	@Override
	public <P extends Page> INoMappableParameterMainState<
			IPageLinkDescriptor,
			Void, Void, Void
			> page(Class<P> pageClass) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.page(), Model.of(pageClass))
		);
	}

	@Override
	public INoMappableParameterMainState<
			IPageLinkDescriptor,
			Void, Void, Void
			> page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.page(), pageClassModel)
		);
	}
	
	@Override
	public IPageInstanceState<IPageLinkGenerator> pageInstance(Page page) {
		return start(page);
	}
	
	@Override
	public IPageInstanceState<IPageLinkGenerator> pageInstance(IModel<? extends Page> pageInstanceModel) {
		return start(pageInstanceModel);
	}

	@Override
	public INoMappableParameterMainState<
			IResourceLinkDescriptor,
			Void, Void, Void
			> resource(ResourceReference resourceReference) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.resource(), Model.of(resourceReference))
		);
	}

	@Override
	public INoMappableParameterMainState<
			IResourceLinkDescriptor,
			Void, Void, Void
			> resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.resource(), resourceReferenceModel)
		);
	}

	@Override
	public INoMappableParameterMainState<
			IImageResourceLinkDescriptor,
			Void, Void, Void
			> imageResource(ResourceReference resourceReference) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.imageResource(), Model.of(resourceReference))
		);
	}

	@Override
	public INoMappableParameterMainState<
			IImageResourceLinkDescriptor,
			Void, Void, Void
			> imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.imageResource(), resourceReferenceModel)
		);
	}

}
