package org.iglooproject.wicket.more.link.dto.component;

import igloo.wicket.model.Detachables;
import java.util.Objects;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.iglooproject.wicket.more.link.descriptor.IBookmarkablePageLink;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.dto.util.LinkInvalidityStrategy;

public class PageLinkBookmarkablePageLink<P extends Page> extends Link<Void>
    implements IBookmarkablePageLink {

  private static final long serialVersionUID = 1L;

  private final IPageLinkGenerator<P> generator;

  private LinkInvalidityStrategy invalidityStrategy = LinkInvalidityStrategy.DISABLE;

  private boolean absolute = false;

  public PageLinkBookmarkablePageLink(String id, IPageLinkGenerator<P> generator) {
    super(id);
    this.generator = Objects.requireNonNull(generator);
  }

  @Override
  public PageLinkBookmarkablePageLink<P> hideIfInvalid() {
    this.invalidityStrategy = LinkInvalidityStrategy.HIDE;
    return this;
  }

  public PageLinkBookmarkablePageLink<P> disableIfInvalid() {
    this.invalidityStrategy = LinkInvalidityStrategy.DISABLE;
    return this;
  }

  public PageLinkBookmarkablePageLink<P> absolute() {
    this.absolute = true;
    return this;
  }

  @Override
  protected CharSequence getURL() {
    return absolute ? generator.fullUrl() : generator.url();
  }

  @Override
  protected boolean linksTo(Page page) {
    return Objects.equals(generator.getPageClass(), page.getClass());
  }

  /**
   * The link is rendered with the bookmarkable URL of the target page: this method is only called
   * by WicketTester (clickLink).
   */
  @Override
  public void onClick() {
    throw generator.restartResponseException();
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
