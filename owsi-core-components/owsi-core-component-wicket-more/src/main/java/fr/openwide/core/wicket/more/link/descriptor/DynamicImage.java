package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IValidatorState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

/**
 * A {@link Image} whose targeted {@link ResourceReference} and {@link PageParameters} may change during the page life cycle (for instance on an Ajax refresh).
 * <p><strong>WARNING:</strong> if this image is rendered while its parameters are invalid, then a {@link LinkParameterValidationRuntimeException}
 * will be thrown when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag) onComponentTag}. Similarly, if the target ResourceReference is invalid,
 * then a {@link LinkInvalidTargetRuntimeException} will be thrown.
 * This is an expected behavior: you should either ensure that your target and parameters are always valid, or that this link is hidden when they are not.
 * The latter can be obtained by either using {@link #setAutoHideIfInvalid(boolean) setAutoHideIfInvalid(true)}, or adding custom {@link Behavior behaviors}
 * using the {@link #setVisibilityAllowed(boolean)} method.
 * @see LinkInvalidTargetRuntimeException
 * @see LinkParameterValidationRuntimeException
 * @see LinkDescriptorBuilder
 * @see IAddedParameterMappingState#mandatory()
 * @see IAddedParameterMappingState#optional()
 * @see IValidatorState#validator(fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator)
 */
public class DynamicImage extends Image {

	private static final long serialVersionUID = 9089339534901852292L;
	
	private final IModel<PageParameters> parametersMapping;
	private final ILinkParameterValidator parametersValidator;
	
	private boolean autoHideIfInvalid = false;
	
	private final IModel<? extends ResourceReference> resourceReferenceModel;

	public DynamicImage(
			String wicketId,
			IModel<? extends ResourceReference> resourceReferenceModel,
			IModel<PageParameters> parametersMapping,
			ILinkParameterValidator parametersValidator) {
		super(wicketId);
		Args.notNull(resourceReferenceModel, "resourceReferenceModel");
		this.resourceReferenceModel = wrap(resourceReferenceModel);
		this.parametersMapping = parametersMapping;
		this.parametersValidator = parametersValidator;
	}
	
	/**
	 * Same as {@link AbstractDynamicBookmarkableLink#isAutoHideIfInvalid()}
	 */
	public boolean isAutoHideIfInvalid() {
		return autoHideIfInvalid;
	}

	/**
	 * Same as {@link AbstractDynamicBookmarkableLink#setAutoHideIfInvalid()}
	 */
	public DynamicImage setAutoHideIfInvalid(boolean autoHideIfInvalid) {
		this.autoHideIfInvalid = autoHideIfInvalid;
		return this;
	}
	
	private PageParameters getParameters() {
		return parametersMapping.getObject();
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();

		if (autoHideIfInvalid) {
			setVisible(false);
			if (isTargetValid()) {
				if (LinkParameterValidators.isModelValid(parametersValidator)) {
					PageParameters parameters = getParameters();
					if (LinkParameterValidators.isSerializedValid(parameters, parametersValidator)) {
						setVisible(true);
					}
				}
			}
		} else {
			setVisible(true);
		}
	}
	
	private boolean isTargetValid() {
		return getImageResourceReference() != null;
	}
	
	@Override
	protected final ResourceReference getImageResourceReference() {
		ResourceReference resourceReference = resourceReferenceModel.getObject();
		return resourceReference;
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		ResourceReference resourceReference = getImageResourceReference();
		if (resourceReference == null) {
			throw new IllegalStateException("The target ResourceReference of an image of type " + getClass() + " was null when trying to render the url.");
		}
		
		PageParameters parameters;

		try {
			LinkParameterValidators.checkModel(parametersValidator);
			parameters = getParameters();
			LinkParameterValidators.checkSerialized(parameters, parametersValidator);
		} catch(LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
		
		setImageResourceReference(resourceReference, parameters);
		
		super.onComponentTag(tag);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		parametersMapping.detach();
		resourceReferenceModel.detach();
	}

}
