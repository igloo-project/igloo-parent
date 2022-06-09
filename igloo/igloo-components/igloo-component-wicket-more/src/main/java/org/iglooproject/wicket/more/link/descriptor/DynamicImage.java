package org.iglooproject.wicket.more.link.descriptor;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.validator.IValidatorState;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

import igloo.wicket.condition.Condition;

/**
 * A {@link Image} whose targeted {@link ResourceReference} and {@link PageParameters} may change during the page life
 * cycle (for instance on an Ajax refresh).
 * <p><strong>WARNING:</strong> if this image is rendered while its parameters are invalid, then a
 * {@link LinkParameterValidationRuntimeException} will be thrown when executing
 * {@link #onComponentTag(org.apache.wicket.markup.ComponentTag) onComponentTag}. Similarly, if the target
 * {@link ResourceReference} is invalid, then a {@link LinkInvalidTargetRuntimeException} will be thrown.
 * <br/>This is an expected behavior: you should either ensure that your target and parameters are always valid,
 * or that this image is hidden when they are not.
 * <br />The latter can be obtained by either using {@link #hideIfInvalid()}, or adding custom
 * {@link Behavior behaviors} that would use the {@link #setVisibilityAllowed(boolean)} method.
 * @see LinkInvalidTargetRuntimeException
 * @see LinkParameterValidationRuntimeException
 * @see LinkDescriptorBuilder
 * @see IAddedParameterMappingState#mandatory()
 * @see IAddedParameterMappingState#optional()
 * @see IChosenParameterState#validator(org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory)
 * @see IChosenParameterState#validator(org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory)
 * @see IValidatorState#validator(org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator)
 */
public class DynamicImage extends Image {

	private static final long serialVersionUID = 9089339534901852292L;
	
	private final IModel<PageParameters> parametersMapping;
	private final ILinkParameterValidator parametersValidator;
	
	private boolean hideIfInvalid = false;
	
	private final IModel<? extends ResourceReference> resourceReferenceModel;
	
	/*
	 * Defaults to false, since Wicket's default to true:
	 * 1. Causes performance issues: if the same image is on a given page multiple times, the browser is forced to
	 *    download it multiple times anyway.
	 * 2. Is useless most of the time, as usage is to add the date of last modification to the query parameter, so
	 *    that cache is used only if the image has not been changed since last download.
	 */
	private Condition shouldAddAntiCacheParameterCondition = Condition.alwaysFalse();

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
	 * Sets the link up so that it will automatically hide (using {@link #setVisible(boolean)}) when its target or parameters are invalid.
	 * <p>Default behavior is throwing a {@link LinkInvalidTargetRuntimeException} or a {@link LinkParameterValidationRuntimeException}
	 * if the target or the parameters are found to be invalid when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag)}.
	 */
	public DynamicImage hideIfInvalid() {
		this.hideIfInvalid = true;
		return this;
	}
	
	private PageParameters getParameters() {
		return parametersMapping.getObject();
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();

		if (hideIfInvalid) {
			setVisible(isValid());
		} else {
			setVisible(true);
		}
	}
	
	private boolean isValid() {
		if (getImageResourceReference() != null) {
			if (LinkParameterValidators.isModelValid(parametersValidator)) {
				PageParameters parameters = getParameters();
				if (LinkParameterValidators.isSerializedValid(parameters, parametersValidator)) {
					return true;
				}
			}
		}
		
		return false;
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
	
	/**
	 * Sets whether Wicket should automatically add a timestamp parameter so as to bypass browser cache.
	 * <strong>Warning:</strong> use with caution, since this bypasses browser cache completely. If there are multiple
	 * references to the same image in a single page, the browser will have to download the image once for each
	 * reference.
	 * @param shouldAddAntiCacheParameterCondition The condition on which to add the anticache parameter automatically.
	 */
	public DynamicImage setShouldAddAntiCacheParameterCondition(Condition shouldAddAntiCacheParameterCondition) {
		this.shouldAddAntiCacheParameterCondition = shouldAddAntiCacheParameterCondition;
		return this;
	}
	
	@Override
	protected final boolean shouldAddAntiCacheParameter() {
		return shouldAddAntiCacheParameterCondition.applies();
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		parametersMapping.detach();
		resourceReferenceModel.detach();
	}

}
