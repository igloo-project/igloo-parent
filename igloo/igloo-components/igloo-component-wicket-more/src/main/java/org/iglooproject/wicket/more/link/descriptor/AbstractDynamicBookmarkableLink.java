package org.iglooproject.wicket.more.link.descriptor;

import org.apache.wicket.markup.html.link.Link;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.validator.IValidatorState;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

/**
 * A {@link Link} whose parameters may change during the page life cycle (for instance on an Ajax
 * refresh).
 *
 * <p><strong>WARNING:</strong> if this link is rendered while its parameters are invalid, then a
 * {@link LinkParameterValidationRuntimeException} will be thrown when executing {@link
 * #onComponentTag(org.apache.wicket.markup.ComponentTag) onComponentTag}. Similarly, if the target
 * is invalid, then a {@link LinkInvalidTargetRuntimeException} will be thrown. <br>
 * By default, this should not happen as the link will be disabled unless it is completely valid.
 * See {@link #disableIfInvalid()} (the default), {@link #hideIfInvalid()} and {@link
 * #throwExceptionIfInvalid()} for more information.
 *
 * @see LinkInvalidTargetRuntimeException
 * @see LinkParameterValidationRuntimeException
 * @see LinkDescriptorBuilder
 * @see IAddedParameterMappingState#mandatory()
 * @see IAddedParameterMappingState#optional()
 * @see
 *     IChosenParameterState#validator(org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory)
 * @see
 *     IChosenParameterState#validator(org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory)
 * @see
 *     IValidatorState#validator(org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator)
 */
public abstract class AbstractDynamicBookmarkableLink extends Link<Void> {

  private static final long serialVersionUID = 1L;

  private static enum BehaviorIfInvalid {
    THROW_EXCEPTION {
      @Override
      protected void onConfigure(AbstractDynamicBookmarkableLink link) {
        link.setVisible(true); // If invalid, the exception will be thrown when rendering
      }
    },
    HIDE {
      @Override
      protected void onConfigure(AbstractDynamicBookmarkableLink link) {
        link.setVisible(link.isValid());
      }
    },
    DISABLE {
      @Override
      protected void onConfigure(AbstractDynamicBookmarkableLink link) {
        link.setEnabled(link.isValid());
      }
    };

    protected abstract void onConfigure(AbstractDynamicBookmarkableLink link);
  }

  private BehaviorIfInvalid behaviorIfInvalid = BehaviorIfInvalid.DISABLE;

  private boolean absolute = false;

  public AbstractDynamicBookmarkableLink(String wicketId) {
    super(wicketId);
  }

  /**
   * Sets the link up so that it will automatically hide (using {@link #setVisible(boolean)}) when
   * its target or parameters are invalid.
   *
   * <p>Default behavior is to automatically disable the link.
   */
  public AbstractDynamicBookmarkableLink hideIfInvalid() {
    this.behaviorIfInvalid = BehaviorIfInvalid.HIDE;
    return this;
  }

  /**
   * Sets the link up so that it will automatically be disabled when its target or parameters are
   * invalid.
   *
   * @deprecated This is the default behavior, so calling this method is generally useless. The
   *     method is here for compatibility reasons.
   */
  @Deprecated
  public AbstractDynamicBookmarkableLink disableIfInvalid() {
    this.behaviorIfInvalid = BehaviorIfInvalid.DISABLE;
    return this;
  }

  /**
   * Sets the link up so that it will throw a {@link LinkInvalidTargetRuntimeException} or a {@link
   * LinkParameterValidationRuntimeException} if the target or the parameters are found to be
   * invalid when executing {@link #onComponentTag(org.apache.wicket.markup.ComponentTag)}.
   *
   * <p>Default behavior is to automatically disable the link.
   */
  public AbstractDynamicBookmarkableLink throwExceptionIfInvalid() {
    this.behaviorIfInvalid = BehaviorIfInvalid.THROW_EXCEPTION;
    return this;
  }

  public boolean isAbsolute() {
    return absolute;
  }

  /**
   * Sets whether the link should be absolute or relative.
   *
   * <p>Default is false: the link is relative by default.
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

    behaviorIfInvalid.onConfigure(this);
  }

  @Override
  protected final CharSequence getURL()
      throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException {
    if (isAbsolute()) {
      return getAbsoluteURL();
    } else {
      return getRelativeURL();
    }
  }

  protected abstract CharSequence getRelativeURL()
      throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException;

  protected abstract CharSequence getAbsoluteURL()
      throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException;

  @Override
  protected boolean getStatelessHint() {
    return true;
  }

  /** No click event is allowed. */
  @Override
  public void onClick() {
    // Unused
  }
}
