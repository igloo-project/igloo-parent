package org.iglooproject.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.Page;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;

/**
 * A container object for storing the various {@link IBuilderLinkDescriptorFactory}.
 */
public class BuilderTargetFactories
		<
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		> {
	
	public static BuilderTargetFactories<
			IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor
			> late() {
		return new BuilderTargetFactories<>(
				BuilderLinkDescriptorFactory.page(),
				BuilderLinkDescriptorFactory.resource(),
				BuilderLinkDescriptorFactory.imageResource()
		);
	}
	
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
		this.lateTargetDefinitionPageLinkDescriptorFactory = lateTargetDefinitionPageLinkDescriptorFactory;
		this.lateTargetDefinitionResourceLinkDescriptorFactory = lateTargetDefinitionResourceLinkDescriptorFactory;
		this.lateTargetDefinitionImageResourceLinkDescriptorFactory = lateTargetDefinitionImageResourceLinkDescriptorFactory;
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
