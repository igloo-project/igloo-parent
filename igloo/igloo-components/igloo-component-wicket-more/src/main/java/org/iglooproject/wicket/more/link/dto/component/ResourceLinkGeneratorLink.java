package org.iglooproject.wicket.more.link.dto.component;

import igloo.wicket.model.Detachables;
import java.util.Objects;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.iglooproject.wicket.more.link.descriptor.IBookmarkablePageLink;
import org.iglooproject.wicket.more.link.dto.base.IResourceLinkGenerator;
import org.iglooproject.wicket.more.link.dto.util.LinkInvalidityStrategy;

/** Resource counterpart of {@link PageLinkBookmarkablePageLink}. */
public class ResourceLinkGeneratorLink extends Link<Void> implements IBookmarkablePageLink {

  private static final long serialVersionUID = 1L;

  private final IResourceLinkGenerator generator;

  private LinkInvalidityStrategy invalidityStrategy = LinkInvalidityStrategy.DISABLE;

  private boolean absolute = false;

  public ResourceLinkGeneratorLink(String id, IResourceLinkGenerator generator) {
    super(id);
    this.generator = Objects.requireNonNull(generator);
  }

  @Override
  public ResourceLinkGeneratorLink hideIfInvalid() {
    this.invalidityStrategy = LinkInvalidityStrategy.HIDE;
    return this;
  }

  public ResourceLinkGeneratorLink disableIfInvalid() {
    this.invalidityStrategy = LinkInvalidityStrategy.DISABLE;
    return this;
  }

  public ResourceLinkGeneratorLink absolute() {
    this.absolute = true;
    return this;
  }

  @Override
  protected CharSequence getURL() {
    return absolute ? generator.fullUrl() : generator.url();
  }

  /**
   * The link is rendered with the URL of the resource: this method is only called by WicketTester
   * (clickLink).
   */
  @Override
  public void onClick() {
    throw new RedirectToUrlException(generator.url());
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();
    invalidityStrategy.onConfigure(this, generator.isValid());
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(generator);
  }
}
