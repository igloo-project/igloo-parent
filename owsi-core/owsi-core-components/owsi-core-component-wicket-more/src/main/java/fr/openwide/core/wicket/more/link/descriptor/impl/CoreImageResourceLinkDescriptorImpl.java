package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.DynamicImage;
import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public class CoreImageResourceLinkDescriptorImpl extends CoreResourceLinkDescriptorImpl
		implements IImageResourceLinkDescriptor {

	private static final long serialVersionUID = 1L;

	public CoreImageResourceLinkDescriptorImpl(IModel<? extends ResourceReference> resourceReferenceModel,
			LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
		super(resourceReferenceModel, parametersMapping, validator);
	}

	@Override
	public DynamicImage image(String wicketId) {
		return new DynamicImage(wicketId, resourceReferenceModel, parametersMapping, parametersValidator);
	}

}
