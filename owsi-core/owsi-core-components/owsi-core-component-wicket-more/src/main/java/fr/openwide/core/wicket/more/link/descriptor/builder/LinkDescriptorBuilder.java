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
 * <li>The link validations (is a permission required on one of the parameters, ...)
 * </ul>
 * <h1>Backward compatibility regarding target definitions</h1>
 * <p>Due to backward-compatibility constraints, target definition may get a bit confusing.
 * <p>For new uses of this class to build links to bookmarkable pages or resources, please use late target definition:
 * begin your building code with <code>LinkDescriptorBuilder.start()</code> and finish in with one of the methods in
 * {@link ILateTargetDefinitionTerminalState}. After that, refrain from calling
 * {@link IEarlyTargetDefinitionTerminalState#build()} since it will only return an unusable link descriptor or mapper.
 * <p>For building link descriptors to already-instantiated pages, please use {@link #toPageInstance(Page)} or
 * {@link #toPageInstance(IModel)}
 * <p>You may encounter legacy code using
 * {@link LinkDescriptorBuilder#LinkDescriptorBuilder() LinkDescriptorBuilder's constructor} and
 * {@link IEarlyTargetDefinitionTerminalState#build()}. This kind of use is called "early target definition" in this
 * API's javadoc, and is deprecated because it does not allow defining dynamic targets based on parameters (as in
 * {@link IChosenParameterState#page(fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory)} for instance).
 */
@SuppressWarnings("deprecation")
public class LinkDescriptorBuilder implements IBaseState {
	
	/**
	 * Start building a link descriptor or a link descriptor mapper that will point to a page or to a resource.
	 */
	public static INoMappableParameterMainState<
			Void,
			IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
			> start() {
		return new NoMappableParameterMainStateImpl<>(BuilderTargetFactories.late());
	}
	
	/**
	 * Start building a link descriptor or a link descriptor mapper that will point to an already-instantiated page.
	 * <p>This type of link descriptor hasn't got any parameter.
	 */
	public static IPageInstanceState<IPageLinkGenerator> toPageInstance(Page page) {
		return toPageInstance(new PageModel<>(page));
	}

	/**
	 * Start building a link descriptor or a link descriptor mapper that will point to an already-instantiated page.
	 * <p>This type of link descriptor hasn't got any parameter.
	 */
	public static IPageInstanceState<IPageLinkGenerator> toPageInstance(IModel<? extends Page> pageInstanceModel) {
		return new CoreLinkDescriptorBuilderPageInstanceStateImpl(pageInstanceModel);
	}
	
	/**
	 * @deprecated This constructor is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	public LinkDescriptorBuilder() { }

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	@Override
	public <P extends Page> INoMappableParameterMainState<
			IPageLinkDescriptor,
			Void, Void, Void
			> page(Class<P> pageClass) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.page(), Model.of(pageClass))
		);
	}

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	@Override
	public INoMappableParameterMainState<
			IPageLinkDescriptor,
			Void, Void, Void
			> page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.page(), pageClassModel)
		);
	}

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	@Override
	public IPageInstanceState<IPageLinkGenerator> pageInstance(Page page) {
		return toPageInstance(page);
	}

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	@Override
	public IPageInstanceState<IPageLinkGenerator> pageInstance(IModel<? extends Page> pageInstanceModel) {
		return toPageInstance(pageInstanceModel);
	}

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	@Override
	public INoMappableParameterMainState<
			IResourceLinkDescriptor,
			Void, Void, Void
			> resource(ResourceReference resourceReference) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.resource(), Model.of(resourceReference))
		);
	}

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	@Override
	public INoMappableParameterMainState<
			IResourceLinkDescriptor,
			Void, Void, Void
			> resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.resource(), resourceReferenceModel)
		);
	}

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
	@Override
	public INoMappableParameterMainState<
			IImageResourceLinkDescriptor,
			Void, Void, Void
			> imageResource(ResourceReference resourceReference) {
		return new NoMappableParameterMainStateImpl<>(
				BuilderTargetFactories.early(BuilderLinkDescriptorFactory.imageResource(), Model.of(resourceReference))
		);
	}

	/**
	 * @deprecated This method is a deprecated way of starting the build of a LinkDescriptor or a
	 * LinkDescriptorMapper. Please see the section about backward compatibility in {@link LinkDescriptorBuilder}.
	 */
	@Deprecated
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
