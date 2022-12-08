package org.iglooproject.wicket.more.link.descriptor.builder.impl.factory;

import java.util.List;
import java.util.Map;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterMappingEntryBuilder;
import org.iglooproject.wicket.more.link.descriptor.impl.CoreImageResourceLinkDescriptorImpl;
import org.iglooproject.wicket.more.link.descriptor.impl.CorePageLinkDescriptorImpl;
import org.iglooproject.wicket.more.link.descriptor.impl.CoreResourceLinkDescriptorImpl;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import igloo.wicket.factory.IDetachableFactory;

public abstract class BuilderLinkDescriptorFactory<TTarget, TLinkDescriptor>
		implements IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> {

	private static final long serialVersionUID = 1L;
	
	private BuilderLinkDescriptorFactory() { }
	
	@Override
	public TLinkDescriptor create(IModel<? extends TTarget> target,
			Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
			Iterable<? extends ILinkParameterValidator> validators) {
		LinkParametersMapping parametersMapping = new LinkParametersMapping(parameterMappingEntries);
		ILinkParameterValidator validator = LinkParameterValidators.chain(validators);
		return create(target, parametersMapping, validator);
	}
	
	protected abstract TLinkDescriptor create(IModel<? extends TTarget> target, LinkParametersMapping parametersMapping,
			ILinkParameterValidator validator);
	
	@Override
	public IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> forMapper(
			Pair<
					? extends IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>>,
					? extends List<Integer>
					> targetFactory,
			Map<LinkParameterMappingEntryBuilder<?>, List<Integer>> mappingEntryBuilders,
			Map<ILinkParameterValidatorFactory<?>, List<Integer>> validatorFactories) {
		return new BuilderMapperLinkDescriptorFactory<>(
				this, targetFactory, mappingEntryBuilders, validatorFactories
		);
	}
	
	@SuppressWarnings("unchecked") // Works for any T that extends Serializable
	public static <T> IBuilderLinkDescriptorFactory<T, Void> none() {
		return NONE;
	}

	@SuppressWarnings("rawtypes")
	private static final BuilderLinkDescriptorFactory NONE =
			new BuilderLinkDescriptorFactory<Object, Void>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			// Nothing to do
		}

		@Override
		protected Void create(IModel<?> target,
				LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
			return null;
		}
		
		private Object readResolve() {
			return NONE;
		}
	};

	public static IBuilderLinkDescriptorFactory<Class<? extends Page>, IPageLinkDescriptor> page() {
		return PAGE;
	}

	private static final BuilderLinkDescriptorFactory<Class<? extends Page>, IPageLinkDescriptor> PAGE =
			new BuilderLinkDescriptorFactory<Class<? extends Page>, IPageLinkDescriptor>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			// Nothing to do
		}

		@Override
		protected IPageLinkDescriptor create(IModel<? extends Class<? extends Page>> target,
				LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
			return new CorePageLinkDescriptorImpl(target, parametersMapping, validator);
		}

		private Object readResolve() {
			return PAGE;
		}
	};

	public static IBuilderLinkDescriptorFactory<ResourceReference, IResourceLinkDescriptor> resource() {
		return RESOURCE;
	}

	private static final BuilderLinkDescriptorFactory<ResourceReference, IResourceLinkDescriptor> RESOURCE =
			new BuilderLinkDescriptorFactory<ResourceReference, IResourceLinkDescriptor>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			// Nothing to do
		}

		@Override
		protected IResourceLinkDescriptor create(IModel<? extends ResourceReference> target,
				LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
			return new CoreResourceLinkDescriptorImpl(target, parametersMapping, validator);
		}

		private Object readResolve() {
			return RESOURCE;
		}
	};

	public static IBuilderLinkDescriptorFactory<ResourceReference, IImageResourceLinkDescriptor> imageResource() {
		return IMAGE_RESOURCE;
	}

	private static final BuilderLinkDescriptorFactory<ResourceReference, IImageResourceLinkDescriptor> IMAGE_RESOURCE =
			new BuilderLinkDescriptorFactory<ResourceReference, IImageResourceLinkDescriptor>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
			// Nothing to do
		}

		@Override
		protected IImageResourceLinkDescriptor create(IModel<? extends ResourceReference> target,
				LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
			return new CoreImageResourceLinkDescriptorImpl(target, parametersMapping, validator);
		}

		private Object readResolve() {
			return IMAGE_RESOURCE;
		}
	};
}
