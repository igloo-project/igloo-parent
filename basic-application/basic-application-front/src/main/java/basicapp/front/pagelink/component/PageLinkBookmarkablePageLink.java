package basicapp.front.pagelink.component;

import basicapp.front.pagelink.base.IPageLinkGenerator;
import basicapp.front.pagelink.util.PageLinkInvalidityStrategy;
import igloo.wicket.model.Detachables;
import java.util.Objects;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;

public class PageLinkBookmarkablePageLink<P extends Page> extends Link<Void> {

  private static final long serialVersionUID = 1L;

  private final IPageLinkGenerator<P> generator;

  private PageLinkInvalidityStrategy invalidityStrategy = PageLinkInvalidityStrategy.DISABLE;

  private boolean absolute = false;

  public PageLinkBookmarkablePageLink(String id, IPageLinkGenerator<P> generator) {
    super(id);
    this.generator = Objects.requireNonNull(generator);
  }

  public PageLinkBookmarkablePageLink<P> hideIfInvalid() {
    this.invalidityStrategy = PageLinkInvalidityStrategy.HIDE;
    return this;
  }

  public PageLinkBookmarkablePageLink<P> disableIfInvalid() {
    this.invalidityStrategy = PageLinkInvalidityStrategy.DISABLE;
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
    return Objects.equals(generator.getClass(), page.getClass());
  }

  @Override
  public void onClick() {
    // nothing to do
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
