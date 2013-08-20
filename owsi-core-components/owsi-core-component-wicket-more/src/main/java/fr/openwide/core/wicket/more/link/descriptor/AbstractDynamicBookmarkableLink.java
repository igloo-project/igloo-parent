package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.builder.CoreLinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IValidatorState;
import fr.openwide.core.wicket.more.link.descriptor.validator.IParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidators;

/**
 * A {@link Link} whose parameters may change during the page life cycle (for instance on an Ajax refresh).
 * <p><strong>WARNING:</strong> if this link is rendered while its parameters are invalid, then a {@link ParameterValidationException}
 * will be thrown when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag) onComponentTag}.
 * This is an expected behavior: you should either ensure that your parameters are always valid, or that this link is hidden when they are not.
 * The latter can be obtained by either using {@link #setAutoHideIfInvalid(boolean) setAutoHideIfInvalid(true)}, or adding custom {@link Behavior behaviors}
 * using the {@link #setVisibilityAllowed(boolean)} method.
 * @see ParameterValidationException
 * @see CoreLinkDescriptorBuilder
 * @see IAddedParameterState#mandatory()
 * @see IAddedParameterState#optional()
 * @see IValidatorState#validator(fr.openwide.core.wicket.more.link.descriptor.validator.IParameterValidator)
 */
public abstract class AbstractDynamicBookmarkableLink extends Link<Void> {
	
	private static final long serialVersionUID = 1L;
	
	private final IModel<PageParameters> parametersModel;
	private final IParameterValidator parametersValidator;
	
	private boolean autoHideIfInvalid = false;

	public AbstractDynamicBookmarkableLink(
			String wicketId,
			IModel<PageParameters> parametersModel,
			IParameterValidator parametersValidator) {
		super(wicketId);
		Args.notNull(parametersModel, "parametersModel");
		Args.notNull(parametersValidator, "parametersValidator");
		this.parametersModel = wrap(parametersModel);
		this.parametersValidator = parametersValidator;
	}
	
	/**
	 * Gets whether this link will hide when its parameters are invalid.
	 * <p>Default is false.
	 * <p>Note: if autohide is disabled, then a {@link ParameterValidationException} may be thrown if the parameters
	 * are found to be invalid when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag)}.
	 * @return True if this link is hidden when its parameters are invalid, false otherwise.
	 */
	public boolean isAutoHideIfInvalid() {
		return autoHideIfInvalid;
	}
	
	/**
	 * Sets whether this link will hide when its parameters are invalid.
	 * <p>Default is false.
	 * <p>Note: if autohide is disabled, then a {@link ParameterValidationException} may be thrown if the parameters
	 * are found to be invalid when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag)}.
	 * @param autoHideIfInvalid True to enable auto hiding, false to disable it.
	 */
	public AbstractDynamicBookmarkableLink setAutoHideIfInvalid(boolean autoHideIfInvalid) {
		this.autoHideIfInvalid = autoHideIfInvalid;
		return this;
	}
	
	private PageParameters getParameters() {
		return parametersModel.getObject();
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		PageParameters parameters = getParameters();
		if (autoHideIfInvalid) {
			setVisible(ParameterValidators.isValid(parameters, parametersValidator));
		} else {
			setVisible(true);
		}
	}
	
	@Override
	protected final CharSequence getURL() throws ParameterValidationException {
		PageParameters parameters = getParameters();
		
		ParameterValidators.check(parameters, parametersValidator);
		
		return getURL(parameters);
	}
	
	protected abstract CharSequence getURL(PageParameters parameters);

	@Override
	protected final boolean getStatelessHint() {
		return false; // This component might be stateful (due to parametersModel)
	}
	
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
		parametersModel.detach();
	}

}
