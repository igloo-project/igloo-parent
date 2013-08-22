package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import org.apache.wicket.Page;
import org.apache.wicket.core.util.lang.WicketObjects;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.springframework.util.Assert;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.impl.CorePageLinkDescriptorImpl;
import fr.openwide.core.wicket.more.link.descriptor.impl.CoreResourceLinkDescriptorImpl;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public abstract class CoreLinkDescriptorBuilderFactory<T extends ILinkDescriptor> implements IDetachable {

	private static final long serialVersionUID = 1L;
	
	private CoreLinkDescriptorBuilderFactory() { }

	public abstract T create(LinkParametersMapping parametersMapping, ILinkParameterValidator validator);

	public static <P extends Page> CoreLinkDescriptorBuilderFactory<IPageLinkDescriptor> page(Class<P> pageClass) {
		final String className = pageClass.getName();

		// Seems to be the "clean" Wicket way to serialize a class object. See BookmarkablePageLink.
		IModel<Class<P>> pageClassModel = 
				new LoadableDetachableModel<Class<P>>() {
					private static final long serialVersionUID = 1L;
					@Override
					protected Class<P> load() {
						return WicketObjects.<P>resolveClass(className);
					}
				};

		return new CorePageLinkDescriptorBuilderFactory(pageClassModel);
	}

	public static CoreLinkDescriptorBuilderFactory<IPageLinkDescriptor> page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return new CorePageLinkDescriptorBuilderFactory(pageClassModel);
	}
	
	private static class CorePageLinkDescriptorBuilderFactory extends CoreLinkDescriptorBuilderFactory<IPageLinkDescriptor> {
		private static final long serialVersionUID = 1L;
		
		final IModel<Class<? extends Page>> readOnlyPageClassModel;
		
		public CorePageLinkDescriptorBuilderFactory(IModel<? extends Class<? extends Page>> pageClassModel) {
			super();
			Assert.notNull(pageClassModel, "pageClassModel cannot be null");
			this.readOnlyPageClassModel = ReadOnlyModel.of(pageClassModel);
		}

		@Override
		public IPageLinkDescriptor create(LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
			return new CorePageLinkDescriptorImpl(readOnlyPageClassModel, parametersMapping, validator);
		}
		
		@Override
		public void detach() {
			readOnlyPageClassModel.detach();
		}
	}
	
	public static CoreLinkDescriptorBuilderFactory<IResourceLinkDescriptor> resource(ResourceReference resourceReference) {
		return new CoreResourceLinkDescriptorBuilderFactory(new Model<ResourceReference>(resourceReference));
	}
	
	public static CoreLinkDescriptorBuilderFactory<IResourceLinkDescriptor> resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new CoreResourceLinkDescriptorBuilderFactory(resourceReferenceModel);
	}
	
	private static class CoreResourceLinkDescriptorBuilderFactory extends CoreLinkDescriptorBuilderFactory<IResourceLinkDescriptor> {
		private static final long serialVersionUID = 1L;
		
		private IModel<ResourceReference> resourceReferenceModel = null;
		
		public CoreResourceLinkDescriptorBuilderFactory(IModel<? extends ResourceReference> resourceReferenceModel) {
			super();
			Assert.notNull(resourceReferenceModel, "resourceReferenceModel cannot be null");
			this.resourceReferenceModel = ReadOnlyModel.of(resourceReferenceModel);
		}
		
		@Override
		public IResourceLinkDescriptor create(LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
			return new CoreResourceLinkDescriptorImpl(resourceReferenceModel, parametersMapping, validator);
		}

		@Override
		public void detach() {
			resourceReferenceModel.detach();
		}
	}

}
