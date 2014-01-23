package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.Url;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IValidatorState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

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
	
	private boolean autoHideIfInvalid = false;
	
	private boolean absolute = false;

	public AbstractDynamicBookmarkableLink(String wicketId) {
		super(wicketId);
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
	
	public boolean isAbsolute() {
		return absolute;
	}

	/**
	 * Sets whether the link should be absolute or relative.
	 * <p> Default is false: the link is relative by default.
	 * 
	 * @param absolute True to make the link absolute.
	 */
	public AbstractDynamicBookmarkableLink setAbsolute(boolean absolute) {
		this.absolute = absolute;
		return this;
	}

	protected abstract boolean isValid();

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		if (isAutoHideIfInvalid()) {
			setVisible(isValid());
		} else {
			setVisible(true);
		}
	}
	
	@Override
	protected final CharSequence getURL() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException {
		CharSequence relativeUrl = getRelativeURL();
		if (isAbsolute()) {
			return getRequestCycle().getUrlRenderer().renderFullUrl(Url.parse(relativeUrl));
		} else {
			return relativeUrl;
		}
	}
	
	protected abstract CharSequence getRelativeURL() throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException;
	
	@Override
	protected boolean getStatelessHint() {
		return true;
	}
	
	/**
	 * No click event is allowed.
	 */
	@Override
	public final void onClick() {
		// Unused
	}

}
