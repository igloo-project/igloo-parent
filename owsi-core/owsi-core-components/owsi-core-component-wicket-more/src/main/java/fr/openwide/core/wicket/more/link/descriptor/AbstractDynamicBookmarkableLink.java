package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IValidatorState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

/**
 * A {@link Link} whose parameters may change during the page life cycle (for instance on an Ajax refresh).
 * <p><strong>WARNING:</strong> if this link is rendered while its parameters are invalid, then a {@link LinkParameterValidationRuntimeException}
 * will be thrown when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag) onComponentTag}. Similarly, if the target is invalid, then a
 * {@link LinkInvalidTargetRuntimeException} will be thrown.
 * This is an expected behavior: you should either ensure that your target and parameters are always valid, or that this link is hidden when they are not.
 * The latter can be obtained by either using {@link #setAutoHideIfInvalid(boolean) setAutoHideIfInvalid(true)}, or adding custom {@link Behavior behaviors}
 * using the {@link #setVisibilityAllowed(boolean)} method.
 * @see LinkInvalidTargetRuntimeException
 * @see LinkParameterSerializedFormValidationException
 * @see LinkDescriptorBuilder
 * @see IAddedParameterMappingState#mandatory()
 * @see IAddedParameterMappingState#optional()
 * @see IValidatorState#validator(fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator)
 */
public abstract class AbstractDynamicBookmarkableLink extends Link<Void> {
	
	private static final long serialVersionUID = 1L;
	
	private final IModel<PageParameters> parametersMapping;
	private final ILinkParameterValidator parametersValidator;
	
	private boolean autoHideIfInvalid = false;

	public AbstractDynamicBookmarkableLink(
			String wicketId,
			IModel<PageParameters> parametersMapping,
			ILinkParameterValidator parametersValidator) {
		super(wicketId);
		Args.notNull(parametersMapping, "parametersMapping");
		Args.notNull(parametersValidator, "parametersValidator");
		this.parametersMapping = wrap(parametersMapping);
		this.parametersValidator = parametersValidator;
	}
	
	/**
	 * Gets whether this link will hide when its target or parameters are invalid.
	 * <p>Default is false.
	 * <p>Note: if autohide is disabled, then a {@link LinkInvalidTargetRuntimeException} or a {@link LinkParameterValidationRuntimeException}
	 * may be thrown if the target or the parameters are found to be invalid when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag)}.
	 * @return True if this link is hidden when its target or parameters are invalid, false otherwise.
	 */
	public boolean isAutoHideIfInvalid() {
		return autoHideIfInvalid;
	}
	
	/**
	 * Sets whether this link will hide when its target or parameters are invalid.
	 * <p>Default is false.
	 * <p>Note: if autohide is disabled, then a {@link LinkInvalidTargetRuntimeException} or a {@link LinkParameterValidationRuntimeException}
	 * may be thrown if the target or the parameters are found to be invalid when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag)}.
	 * @param autoHideIfInvalid True to enable auto hiding, false to disable it.
	 */
	public AbstractDynamicBookmarkableLink setAutoHideIfInvalid(boolean autoHideIfInvalid) {
		this.autoHideIfInvalid = autoHideIfInvalid;
		return this;
	}
	
	private PageParameters getParameters() {
		return parametersMapping.getObject();
	}
	
	protected abstract boolean isTargetValid();
	
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
	
	@Override
	protected final CharSequence getURL() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException {
		PageParameters parameters;
		
		try {
			LinkParameterValidators.checkModel(parametersValidator);
			parameters = getParameters();
			LinkParameterValidators.checkSerialized(parameters, parametersValidator);
		} catch(LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
		
		return getURL(parameters);
	}
	
	@Override
	protected boolean getStatelessHint() {
		return true;
	}
	
	protected abstract CharSequence getURL(PageParameters parameters) throws LinkInvalidTargetRuntimeException;
	
	/**
	 * No click event is allowed.
	 */
	@Override
	public final void onClick() {
		// Unused
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		parametersMapping.detach();
		parametersValidator.detach();
	}

}
