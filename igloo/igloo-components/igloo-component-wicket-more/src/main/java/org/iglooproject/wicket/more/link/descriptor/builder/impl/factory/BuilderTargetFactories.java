package org.iglooproject.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.util.model.Models;

/**
 * A container object for storing the various {@link IBuilderLinkDescriptorFactory}.
 */
public class BuilderTargetFactories
		<
		TEarlyTargetDefinitionLinkDescriptor, TEarlyTarget,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		> {
	
	public static <TEarlyTargetDefinitionLinkDescriptor, TEarlyTarget>
			BuilderTargetFactories<
			TEarlyTargetDefinitionLinkDescriptor, TEarlyTarget,
			Void, Void, Void
			> early(IBuilderLinkDescriptorFactory<TEarlyTarget, TEarlyTargetDefinitionLinkDescriptor> factory,
					IModel<? extends TEarlyTarget> earlyTargetModel) {
		return new BuilderTargetFactories<
				TEarlyTargetDefinitionLinkDescriptor, TEarlyTarget,
				Void, Void, Void
				>(
				factory, earlyTargetModel,
				BuilderLinkDescriptorFactory.<Class<? extends Page>>none(),
				BuilderLinkDescriptorFactory.<ResourceReference>none(),
				BuilderLinkDescriptorFactory.<ResourceReference>none()
		);
	}
	
	public static <TEarlyTargetDefinitionLinkDescriptor> BuilderTargetFactories<
			Void, Void,
			IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
			> late() {
		return new BuilderTargetFactories<
				Void, Void,
				IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
				>(
				BuilderLinkDescriptorFactory.<Void>none(), Models.<Void>placeholder(),
				BuilderLinkDescriptorFactory.page(),
				BuilderLinkDescriptorFactory.resource(),
				BuilderLinkDescriptorFactory.imageResource()
		);
	}
	
	private final IBuilderLinkDescriptorFactory<TEarlyTarget, TEarlyTargetDefinitionLinkDescriptor>
			earlyTargetDefinitionLinkDescriptorFactory;
	private final IModel<? extends TEarlyTarget> earlyTargetDefinitionTargetModel;
	private final IBuilderLinkDescriptorFactory<
			Class<? extends Page>, TLateTargetDefinitionPageLinkDescriptor
			> lateTargetDefinitionPageLinkDescriptorFactory;
	private final IBuilderLinkDescriptorFactory<
			ResourceReference, TLateTargetDefinitionResourceLinkDescriptor
			> lateTargetDefinitionResourceLinkDescriptorFactory;
	private final IBuilderLinkDescriptorFactory<
			ResourceReference, TLateTargetDefinitionImageResourceLinkDescriptor
			> lateTargetDefinitionImageResourceLinkDescriptorFactory;
	
	private BuilderTargetFactories(
			IBuilderLinkDescriptorFactory<TEarlyTarget, TEarlyTargetDefinitionLinkDescriptor>
					earlyTargetDefinitionLinkDescriptorFactory,
			IModel<? extends TEarlyTarget> earlyTargetDefinitionTargetModel,
			IBuilderLinkDescriptorFactory<
					Class<? extends Page>, TLateTargetDefinitionPageLinkDescriptor
					> lateTargetDefinitionPageLinkDescriptorFactory,
			IBuilderLinkDescriptorFactory<
					ResourceReference, TLateTargetDefinitionResourceLinkDescriptor
					> lateTargetDefinitionResourceLinkDescriptorFactory,
			IBuilderLinkDescriptorFactory<
					ResourceReference, TLateTargetDefinitionImageResourceLinkDescriptor
					> lateTargetDefinitionImageResourceLinkDescriptorFactory) {
		super();
		this.earlyTargetDefinitionLinkDescriptorFactory = earlyTargetDefinitionLinkDescriptorFactory;
		this.earlyTargetDefinitionTargetModel = earlyTargetDefinitionTargetModel;
		this.lateTargetDefinitionPageLinkDescriptorFactory = lateTargetDefinitionPageLinkDescriptorFactory;
		this.lateTargetDefinitionResourceLinkDescriptorFactory = lateTargetDefinitionResourceLinkDescriptorFactory;
		this.lateTargetDefinitionImageResourceLinkDescriptorFactory = lateTargetDefinitionImageResourceLinkDescriptorFactory;
	}

	public IBuilderLinkDescriptorFactory<TEarlyTarget, TEarlyTargetDefinitionLinkDescriptor>
			getEarlyTargetDefinitionLinkDescriptorFactory() {
		return earlyTargetDefinitionLinkDescriptorFactory;
	}
	
	public IModel<? extends TEarlyTarget> getEarlyTargetDefinitionTargetModel() {
		return earlyTargetDefinitionTargetModel;
	}

	public IBuilderLinkDescriptorFactory<Class<? extends Page>, TLateTargetDefinitionPageLinkDescriptor>
			getLateTargetDefinitionPageLinkDescriptorFactory() {
		return lateTargetDefinitionPageLinkDescriptorFactory;
	}

	public IBuilderLinkDescriptorFactory<ResourceReference, TLateTargetDefinitionResourceLinkDescriptor>
			getLateTargetDefinitionResourceLinkDescriptorFactory() {
		return lateTargetDefinitionResourceLinkDescriptorFactory;
	}

	public IBuilderLinkDescriptorFactory<ResourceReference, TLateTargetDefinitionImageResourceLinkDescriptor>
			getLateTargetDefinitionImageResourceLinkDescriptorFactory() {
		return lateTargetDefinitionImageResourceLinkDescriptorFactory;
	}

}
